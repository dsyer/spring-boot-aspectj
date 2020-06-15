package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties="logging.level.org.springframework.aop=TRACE")
public class InterceptorApplicationTests {

	@Test
	public void contextLoads() {
	}

}
