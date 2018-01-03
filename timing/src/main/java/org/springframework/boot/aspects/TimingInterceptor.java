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
package org.springframework.boot.aspects;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.util.StopWatch;

/**
 * @author Dave Syer
 *
 */
@Aspect
public class TimingInterceptor {

	private static Log logger = LogFactory.getLog(TimingInterceptor.class);

	private StopWatch bind = new StopWatch("bind");

	private StopWatch init = new StopWatch("init");

	@Around("execution(private * org.springframework.boot.context.properties.ConfigurationPropertiesBindingPostProcessor.postProcessBeforeInitialization(Object, String, ..)) && args(bean,..)")
	public Object bind(ProceedingJoinPoint joinPoint, Object bean) throws Throwable {
		bind.start();
		Object result = joinPoint.proceed();
		bind.stop();
		logger.info("Bind,," + bean.getClass().getName() + ": "
				+ bind.getLastTaskTimeMillis());
		return result;
	}

	@Around("execution(* org.springframework.beans.factory.config.BeanPostProcessor+.*(Object, String)) && args(bean,..)")
	public Object post(ProceedingJoinPoint joinPoint, Object bean) throws Throwable {
		long t0 = System.currentTimeMillis();
		Object result = joinPoint.proceed();
		long t1 = System.currentTimeMillis();
		logger.info("Post," + joinPoint.getSignature().getDeclaringType().getSimpleName()
				+ "." + joinPoint.getSignature().getName() + ","
				+ bean.getClass().getName() + "," + (t1 - t0));
		return result;
	}

	@Around("execution(* org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory+.initializeBean(String, Object, ..)) && args(name,bean,..)")
	public Object init(ProceedingJoinPoint joinPoint, String name, Object bean)
			throws Throwable {
		String task = init.currentTaskName();
		if (task != null) {
			init.stop();
		}
		init.start(name);
		int count0 = init.getTaskCount();
		long t0 = System.currentTimeMillis();
		Object result = joinPoint.proceed();
		long t1 = System.currentTimeMillis();
		int count1 = init.getTaskCount();
		init.stop();
		if (task != null) {
			init.start(task);
		}
		else {
			logger.info("Init,," + bean.getClass().getName() + "," + (t1 - t0));
			logger.info("Count,," + bean.getClass().getName() + "," + (count1 - count0));
		}
		return result;
	}

	@EventListener
	public void started(ContextRefreshedEvent event) {
		logger.info("Total bind: " + bind.getTotalTimeMillis());
		logger.info("Total init: " + init.getTotalTimeMillis());
	}

}