// build.gradle.kts
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    `java-library`
}


group = "uk.gov.hmcts.example"
version = "0.0.1-SNAPSHOT"

object Versions {
	val junitVersion     = "5.10.0"
	val hamcrestVersion  = "2.2"
	val mockitoVersion   = "5.4.0"
	val sl4jVersion      = "2.0.9"

	val testContainerVersion   = "1.19.0"
	val activeMQClientVersion  = "5.18.2"
}

dependencies {

	implementation("org.slf4j:slf4j-api:${Versions.sl4jVersion}")
	implementation("org.slf4j:slf4j-simple:${Versions.sl4jVersion}")

	testImplementation("org.hamcrest:hamcrest:${Versions.hamcrestVersion}")
	testImplementation("org.junit.jupiter:junit-jupiter-api:${Versions.junitVersion}")
	testImplementation("org.junit.jupiter:junit-jupiter-engine:${Versions.junitVersion}")
	testImplementation("org.junit.jupiter:junit-jupiter-params:${Versions.junitVersion}")
	testImplementation("org.mockito:mockito-core:${Versions.mockitoVersion}")
	testImplementation("org.mockito:mockito-junit-jupiter:${Versions.mockitoVersion}")

	testImplementation( "org.testcontainers:testcontainers:${Versions.testContainerVersion}" )
	testImplementation( "org.testcontainers:junit-jupiter:${Versions.testContainerVersion}" )
	testImplementation( "org.apache.activemq:activemq-client:${Versions.activeMQClientVersion}")

}

configurations {                            
    implementation {
        resolutionStrategy.failOnVersionConflict()
    }
}

repositories {
	mavenCentral()
}


java {                                      
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<Test> {
	useJUnitPlatform()

	testLogging {
		events = hashSetOf(
				TestLogEvent.FAILED,
				TestLogEvent.PASSED,
				TestLogEvent.SKIPPED,
				TestLogEvent.STANDARD_OUT,
				TestLogEvent.STANDARD_ERROR
		)
		showStandardStreams = true
	}
}

