package com.example;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

@Aspect
@ConfigurationProperties("interceptor")
public class Interceptor {

	/**
	 * Message to print on startup
	 */
	private String message = "Started";

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Around("execution(* *(..)) && !within(com.example.Interceptor)"
			+ " && (within(org.springframework.context.annotation.Condition+) || within(com.example..*))")
	public Object intercept(ProceedingJoinPoint joinPoint) throws Throwable {
		Object result = joinPoint.proceed();
		System.err.println(joinPoint.toShortString() + ": " + result);
		return result;
	}

	@Around("execution(* *(..)) && within(com.example..*) && !within(com.example.Interceptor+)")
	public Object stack(ProceedingJoinPoint joinPoint) throws Throwable {
		new RuntimeException().printStackTrace();
		return joinPoint.proceed();
	}

	@EventListener
	public void started(ContextRefreshedEvent event) {
		System.err.println(message + ": " + event);
	}

}
