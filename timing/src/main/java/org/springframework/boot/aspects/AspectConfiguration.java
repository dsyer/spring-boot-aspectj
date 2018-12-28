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

import org.aspectj.lang.Aspects;

import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author Dave Syer
 *
 */
@Configuration
public class AspectConfiguration {

	@Bean
	@Conditional(AspectsCondition.class)
	@ConditionalOnClass(name = "org.aspectj.lang.Aspects")
	public TimingInterceptor timingInterceptor() {
		return Aspects.aspectOf(TimingInterceptor.class);
	}

	private static class AspectsCondition extends SpringBootCondition {

		@Override
		public ConditionOutcome getMatchOutcome(ConditionContext context,
				AnnotatedTypeMetadata metadata) {
			if (Aspects.hasAspect(TimingInterceptor.class)) {
				return ConditionOutcome.match("AspectJ weaving");
			}
			return ConditionOutcome.noMatch("AspectJ not weaving");
		}

	}

}
