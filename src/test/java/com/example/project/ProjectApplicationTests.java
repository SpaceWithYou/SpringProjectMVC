package com.example.project;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {"spring.config.location=classpath:application-test.properties"})
class ProjectApplicationTests {

	@Test
	void contextLoads() {
	}

}
