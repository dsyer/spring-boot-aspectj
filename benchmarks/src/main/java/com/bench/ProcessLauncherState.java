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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openjdk.jmh.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.system.JavaVersion;

import com.example.InterceptorApplication;

public class ProcessLauncherState {

	private static final Logger logger = LoggerFactory.getLogger(ProcessLauncherState.class);

	private Process started;
	private List<String> args;
	private List<String> progArgs;
	private List<String> jvmArgs;
	private File home;
	private String mainClass = InterceptorApplication.class.getName();
	private int length;

	public ProcessLauncherState(String dir, String... args) {
		this.args = new ArrayList<>(Arrays.asList(args));
		int count = 0;
		this.args.add(count++, System.getProperty("java.home") + "/bin/java");
		this.args.add(count++, "-Xmx128m");
		this.args.add(count++, "-cp");
		this.args.add(count++, getClasspath());
		this.args.add(count++, "-Djava.security.egd=file:/dev/./urandom");
		this.args.add(count++, "-XX:TieredStopAtLevel=1"); // zoom
		if (System.getProperty("bench.args") != null) {
			this.args.addAll(count++, Arrays.asList(System.getProperty("bench.args").split(" ")));
		}
		this.length = args.length;
		this.home = new File(dir);
	}

	public void setMainClass(String mainClass) {
		this.mainClass = mainClass;
	}

	public void setProgArgs(String... extraArgs) {
		this.progArgs = Arrays.asList(extraArgs);
	}

	public void setJvmArgs(String... extraArgs) {
		this.jvmArgs = Arrays.asList(extraArgs);
	}

	String getClasspath() {
		if (JavaVersion.getJavaVersion().isOlderThan(JavaVersion.NINE)) {
			return getClasspathJdk8();
		}
		return getClasspathJdk9();
	}

	String getClasspathJdk8() {
		StringBuilder builder = new StringBuilder();

		for (URL url : ((URLClassLoader) getClass().getClassLoader()).getURLs()) {
			if (builder.length() > 0) {
				builder.append(File.pathSeparator);
			}
			builder.append(url.toString());
		}

		String resultString = builder.toString();
		logger.info("Result Classpath: " + resultString);

		return resultString;
	}

	String getClasspathJdk9() {
		String classpath = System.getProperty("java.class.path");
		String[] entries = classpath.split(File.pathSeparator);
		URL[] result = new URL[entries.length];

		for (int i = 0; i < entries.length; i++) {
			try {
				result[i] = Paths.get(entries[i]).toAbsolutePath().toUri().toURL();
			} catch (MalformedURLException e) {
				logger.error(e.getStackTrace().toString());
			}
		}

		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < result.length; i++) {
			if (builder.length() > 0) {
				builder.append(File.pathSeparator);
			}
			builder.append(result[i]);
		}

		String resultString = builder.toString();

		logger.info("Result Classpath: " + resultString);

		return resultString;
	}

	public void after() throws Exception {
		if (started != null && started.isAlive()) {
			System.err.println("Stopped " + mainClass + ": " + started.destroyForcibly().waitFor());
		}
	}

	public void run() throws Exception {
		List<String> args = new ArrayList<>(this.args);
		if (jvmArgs != null) {
			args.addAll(1, this.jvmArgs);
		}
		args.add(args.size() - this.length, this.mainClass);
		if (progArgs != null) {
			args.addAll(progArgs);
		}
		ProcessBuilder builder = new ProcessBuilder(args);
		builder.directory(home);
		builder.redirectErrorStream(true);
		customize(builder);
		if (!"false".equals(System.getProperty("debug", "false"))) {
			System.err.println("Running: " + Utils.join(args, " "));
		}
		started = builder.start();
		monitor();
	}

	protected void customize(ProcessBuilder builder) {
	}

	protected void monitor() throws IOException {
		System.out.println(output(started.getInputStream(), "Started"));
	}

	protected static String output(InputStream inputStream, String marker) throws IOException {
		StringBuilder sb = new StringBuilder();
		BufferedReader br = null;
		br = new BufferedReader(new InputStreamReader(inputStream));
		String line = null;
		while ((line = br.readLine()) != null && !line.contains(marker)) {
			sb.append(line + System.getProperty("line.separator"));
		}
		if (line != null) {
			sb.append(line + System.getProperty("line.separator"));
		}
		return sb.toString();
	}

	public File getHome() {
		return home;
	}
}