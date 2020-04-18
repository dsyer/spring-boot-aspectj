package com.bench;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ProcessLauncherStateTest {

	private static final Logger logger = LoggerFactory.getLogger(ProcessLauncherStateTest.class);

	@BeforeEach
	void setUp() throws Exception {
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
	}

}
