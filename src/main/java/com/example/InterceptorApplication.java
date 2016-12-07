package com.example;

import org.aspectj.lang.Aspects;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class InterceptorApplication {

	@RequestMapping("/")
	public String home() {
		return "Hello World";
	}
	
	@Bean
	public Interceptor interceptor() {
		return Aspects.aspectOf(Interceptor.class);
	}

	public static void main(String[] args) {
		new SpringApplicationBuilder(InterceptorApplication.class).run(args);
	}
}
