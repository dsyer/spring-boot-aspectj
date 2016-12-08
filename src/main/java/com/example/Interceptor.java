package com.example;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

@Aspect
public class Interceptor {

	@Around("execution(* *(..))"
			+ " && (within(org.springframework.context.annotation.Condition+) || within(com.example..*))"
			+ " && !within(com.example.Interceptor)")
	public Object intercept(ProceedingJoinPoint joinPoint) throws Throwable {
		System.err.println(joinPoint.toShortString() + ": " + joinPoint.getSignature());
		return joinPoint.proceed();
	}

	@EventListener
	public void started(ContextRefreshedEvent event) {
		System.err.println("Started: " + event);
	}

}
