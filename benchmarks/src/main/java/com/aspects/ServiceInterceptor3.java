/*
 * Copyright 2016-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * @author Dave Syer
 *
 */
@Aspect
class ServiceInterceptor3 {
	@Around("execution (@org.springframework.scheduling.annotation.Async  * *.*(..))")
	public Object intercept(ProceedingJoinPoint joinPoint) throws Throwable {
		Object result = joinPoint.proceed();
		System.err.println(joinPoint.toShortString() + ": " + result);
		return result;
	}
}