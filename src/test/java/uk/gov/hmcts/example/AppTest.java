package uk.gov.hmcts.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class AppTest {
	@DisplayName("TDD: first create a failing test")
	@Test
	public void create_a_failing_test() {		
		assertThat( 1 + 2 + 3, is(6) );
	}
}


