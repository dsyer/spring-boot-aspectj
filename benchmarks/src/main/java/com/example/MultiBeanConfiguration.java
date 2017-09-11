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

package com.example;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author Dave Syer
 *
 */
@Configuration
@Import(MultiBeanSelector.class)
public class MultiBeanConfiguration {
}

class Service {
	public void execute() {
	}
}

@Aspect
class ServiceInterceptor {
	@Around("execution (* com.example..*(..))")
	public Object intercept(ProceedingJoinPoint joinPoint) throws Throwable {
		Object result = joinPoint.proceed();
		System.err.println(joinPoint.toShortString() + ": " + result);
		return result;
	}
}

@Aspect
class AnnotationInterceptor {
	@Around("@annotation(org.springframework.scheduling.annotation.Async) && execution (* com.example..*(..))")
	public Object intercept(ProceedingJoinPoint joinPoint) throws Throwable {
		Object result = joinPoint.proceed();
		System.err.println(joinPoint.toShortString() + ": " + result);
		return result;
	}
}

class MultiBeanSelector implements ImportBeanDefinitionRegistrar {

	private static int beans = 0;

	private static int aspects = 0;

	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
			BeanDefinitionRegistry registry) {
		DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) registry;
		ConfigurableEnvironment environment = (ConfigurableEnvironment) beanFactory
				.getSingleton("environment");
		int total = environment.getProperty("bench.beans", Integer.class, 0);
		for (int i = beans; i < beans + total; i++) {
			registry.registerBeanDefinition("service" + i, BeanDefinitionBuilder
					.rootBeanDefinition(Service.class).getBeanDefinition());
		}
		beans += total;
		total = environment.getProperty("bench.aspects", Integer.class, 0);
		Class<?> interceptorType = ServiceInterceptor.class;
		if (environment.getProperty("bench.annotation", Boolean.class, false)) {
			interceptorType = AnnotationInterceptor.class;
		}
		for (int i = aspects; i < aspects + total; i++) {
			registry.registerBeanDefinition("aspect" + i, BeanDefinitionBuilder
					.rootBeanDefinition(interceptorType).getBeanDefinition());
		}
		aspects += total;
	}

}
