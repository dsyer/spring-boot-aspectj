package com.example;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Aspect
@ConfigurationProperties("interceptor")
@Component
public class Interceptor {

	private static final Logger logger = LoggerFactory.getLogger(Interceptor.class);

	/**
	 * Message to print on startup
	 */
	private String message = "Startup";

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Around("execution(* *.*(..)) && within(com.example..*)")
	public Object intercept(ProceedingJoinPoint joinPoint) throws Throwable {
		Object result = joinPoint.proceed();
		logger.debug("AspectJ intercept: " + joinPoint.toShortString() + ": " + result);
		return result;
	}

	@Around("execution(* *.*(..)) && within(com.example..*)")
	public Object another(ProceedingJoinPoint joinPoint) throws Throwable {
		Object result = joinPoint.proceed();
		logger.debug("AspectJ another: " + joinPoint.toShortString() + ": " + result);
		return result;
	}

	@EventListener
	public void started(ContextRefreshedEvent event) {
		logger.debug("AspectJ started: " + message + ": " + event);
	}

}
