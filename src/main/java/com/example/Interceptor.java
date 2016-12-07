package com.example;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class Interceptor {
	
	@Around("execution(* *(..)) && within(org.springframework.context.annotation.Condition+)")
	public Object intercept(ProceedingJoinPoint joinPoint) throws Throwable {
		System.err.println(joinPoint.toShortString() + ": " + joinPoint.getSignature());
		return joinPoint.proceed();
	}

}
