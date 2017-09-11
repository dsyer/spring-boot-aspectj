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
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;

@Measurement(iterations = 5)
@Warmup(iterations = 1)
@Fork(value = 2, warmups = 0)
@BenchmarkMode(Mode.AverageTime)
public class StartupBenchmark {

	@Benchmark
	public void vanilla(ApplicationState state) throws Exception {
		state.run();
	}

	@Benchmark
	public void vanilla_1_50(ApplicationState state) throws Exception {
		state.setProgArgs("--bench.beans=50", "--bench.aspects=1");
		state.run();
	}

	@Benchmark
	public void vanilla_10_50(ApplicationState state) throws Exception {
		state.setProgArgs("--bench.beans=50", "--bench.aspects=10");
		state.run();
	}

	@Benchmark
	public void vanilla_20_50(ApplicationState state) throws Exception {
		state.setProgArgs("--bench.beans=50", "--bench.aspects=20");
		state.run();
	}

	@Benchmark
	public void vanilla_1_100(ApplicationState state) throws Exception {
		state.setProgArgs("--bench.beans=100", "--bench.aspects=1");
		state.run();
	}

	@Benchmark
	public void vanilla_10_100(ApplicationState state) throws Exception {
		state.setProgArgs("--bench.beans=100", "--bench.aspects=10");
		state.run();
	}

	@Benchmark
	public void vanilla_20_100(ApplicationState state) throws Exception {
		state.setProgArgs("--bench.beans=100", "--bench.aspects=20");
		state.run();
	}

	@Benchmark
	public void annotation_1_50(ApplicationState state) throws Exception {
		state.setProgArgs("--bench.beans=50", "--bench.aspects=1", "--bench.annotation=true");
		state.run();
	}

	@Benchmark
	public void annotation_10_50(ApplicationState state) throws Exception {
		state.setProgArgs("--bench.beans=50", "--bench.aspects=10", "--bench.annotation=true");
		state.run();
	}

	@Benchmark
	public void annotation_20_50(ApplicationState state) throws Exception {
		state.setProgArgs("--bench.beans=50", "--bench.aspects=20", "--bench.annotation=true");
		state.run();
	}

	@Benchmark
	public void annotation_1_100(ApplicationState state) throws Exception {
		state.setProgArgs("--bench.beans=100", "--bench.aspects=1", "--bench.annotation=true");
		state.run();
	}

	@Benchmark
	public void annotation_10_100(ApplicationState state) throws Exception {
		state.setProgArgs("--bench.beans=100", "--bench.aspects=10", "--bench.annotation=true");
		state.run();
	}

	@Benchmark
	public void annotation_20_100(ApplicationState state) throws Exception {
		state.setProgArgs("--bench.beans=100", "--bench.aspects=20", "--bench.annotation=true");
		state.run();
	}

	@Benchmark
	public void ltw(ApplicationState state) throws Exception {
		state.setProgArgs("--spring.aop.auto=false");
		state.setJvmArgs(
				"-javaagent:" + home() + "/.m2/repository/org/aspectj/aspectjweaver/1.8.10/aspectjweaver-1.8.10.jar");
		state.run();
	}

	@Benchmark
	public void ltw_100(ApplicationState state) throws Exception {
		state.setProgArgs("--bench.beans=100", "--spring.aop.auto=false");
		state.setJvmArgs(
				"-javaagent:" + home() + "/.m2/repository/org/aspectj/aspectjweaver/1.8.10/aspectjweaver-1.8.10.jar");
		state.run();
	}

	private String home() {
		return System.getProperty("user.home");
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
