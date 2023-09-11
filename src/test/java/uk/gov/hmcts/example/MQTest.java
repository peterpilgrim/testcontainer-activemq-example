package uk.gov.hmcts.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.jms.*;

import org.apache.activemq.ActiveMQConnectionFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
public class MQTest {

    final static int MQ_PORT = 61616;
    @Container
    public GenericContainer<?> activeMQContainer = new GenericContainer<>(DockerImageName.parse("rmohr/activemq"))
            .withExposedPorts(MQ_PORT);

    private Connection connection;
    private Session session;
    private MessageProducer producer;
    private MessageConsumer consumer;

    @BeforeEach
    public void setup() throws JMSException {
        String brokerUrl = "tcp://localhost:" + activeMQContainer.getMappedPort(MQ_PORT);
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerUrl);
        connection = connectionFactory.createConnection();
        connection.start();

        // Creating session for sending messages
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // Getting the queue
        Queue queue = session.createQueue("testQueue");

        // Creating the producer & consumer
        producer = session.createProducer(queue);
        consumer = session.createConsumer(queue);
    }

    @AfterEach
    public void tearDown() throws JMSException {
        // Cleaning up resources
        if (producer != null) producer.close();
        if (consumer != null) consumer.close();
        if (session != null) session.close();
        if (connection != null) connection.close();
    }

    @DisplayName("send message over a TestContainer")
    @Test
    public void testSendMessage() throws JMSException {
        String dummyPayload = "Dummy payload";

        // Sending a text message to the queue
        TextMessage message = session.createTextMessage(dummyPayload);
        producer.send(message);

        // Receiving the message from the queue
        TextMessage receivedMessage = (TextMessage) consumer.receive(2500);

        assertEquals(dummyPayload, receivedMessage.getText());
    }
}