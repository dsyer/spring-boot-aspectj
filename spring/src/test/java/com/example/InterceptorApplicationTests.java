package com.example;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = { "logging.level.org.springframework.aop=TRACE", "logging.level.com.example=DEBUG" })
public class InterceptorApplicationTests {

	@Test
	public void contextLoads() {
	}

}
