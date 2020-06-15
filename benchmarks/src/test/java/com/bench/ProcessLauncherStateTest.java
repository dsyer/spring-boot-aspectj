package com.bench;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ProcessLauncherStateTest {

	private static final Logger logger = LoggerFactory.getLogger(ProcessLauncherStateTest.class);

	private final String CLASSPATH_PART_RESULT = "spring-boot-aspectj/benchmarks/target/test-classes/";

	@Test
	void testApplication() throws Exception {
		ProcessLauncherState processLauncherState = new ProcessLauncherState("target");
		processLauncherState.run();
		processLauncherState.after();
	}

	@Test
	void testGetClasspathJdk8() {
		ProcessLauncherState processLauncherState = new ProcessLauncherState("", "");
		String classpathName = processLauncherState.getClasspathJdk8();

		logger.info(classpathName);
	}

	@Test
	void testGetClasspathJdk9() {
		ProcessLauncherState processLauncherState = new ProcessLauncherState("", "");
		String classpathName = processLauncherState.getClasspathJdk9();

		logger.info(classpathName);

		assertTrue(classpathName.contains(CLASSPATH_PART_RESULT));
	}

}
