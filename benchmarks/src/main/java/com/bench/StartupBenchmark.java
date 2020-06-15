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
package com.bench;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;

@Measurement(iterations = 5)
@Warmup(iterations = 1)
@Fork(value = 2, warmups = 0)
@BenchmarkMode(Mode.AverageTime)
public class StartupBenchmark {

	private static final String M2_REPOSITORY_ASPECTJWEAVER = "/.m2/repository/org/aspectj/aspectjweaver/1.9.5/aspectjweaver-1.9.5.jar";

	@Benchmark
	public void spring(SpringState state) throws Exception {
		state.run();
	}

	@Benchmark
	public void ltw(ApplicationState state) throws Exception {
		state.setProgArgs("--spring.aop.auto=false");
		state.setJvmArgs("-javaagent:" + home() + M2_REPOSITORY_ASPECTJWEAVER);
		state.run();
	}

	@Benchmark
	public void ltw_100(ApplicationState state) throws Exception {
		state.setProgArgs("--bench.beans=100", "--spring.aop.auto=false");
		state.setJvmArgs("-javaagent:" + home() + M2_REPOSITORY_ASPECTJWEAVER);
		state.run();
	}

	private String home() {
		return System.getProperty("user.home");
	}

	@State(Scope.Benchmark)
	public static class SpringState extends ProcessLauncherState {

		public static enum Scale {
			v0_10(0, 10), v1_10(1, 10), v1_100(1, 100), v10_50(10, 50), v20_50(20, 50), a0_10(0, 10, true),
			a1_10(1, 10, true), a1_100(1, 100, true), a10_50(10, 50, true), a10_100(10, 100, true),
			a20_50(20, 50, true);

			private int beans;
			private int aspects;
			private boolean annotation;

			private Scale(int aspects, int beans) {
				this(aspects, beans, false);
			}

			private Scale(int aspects, int beans, boolean annotation) {
				this.beans = beans;
				this.aspects = aspects;
				this.annotation = annotation;
			}

			public String[] getArgs() {
				return new String[] { "--server.port=0", "--bench.aspects=" + aspects, "--bench.beans=" + beans,
						"--bench.annotation=" + annotation };
			}
		}

		@Param
		private Scale scale = Scale.v1_10;

		public SpringState() {
			super("target");
		}

		@Setup(Level.Iteration)
		public void start() throws Exception {
			setProgArgs(scale.getArgs());
		}

		@TearDown(Level.Iteration)
		public void stop() throws Exception {
			super.after();
		}
	}

	@State(Scope.Benchmark)
	public static class ApplicationState extends ProcessLauncherState {

		public ApplicationState() {
			super("target", "--server.port=0");
		}

		@TearDown(Level.Iteration)
		public void stop() throws Exception {
			super.after();
		}
	}

}
