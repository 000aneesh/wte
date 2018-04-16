package com.app.wte.testengine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.app.wte.model.TestResult;
import com.app.wte.model.TestRunRequest;
import com.app.wte.service.Task;

public abstract class TestSuite implements Runnable {

	@Autowired
	@Qualifier("fileGenerationTask")
	Task fileGenerationTask;

	@Autowired
	@Qualifier("fTPTransferTask")
	Task fTPTransferTask;

	TestResult testResult;

	
	private static final Logger logger = LoggerFactory.getLogger(TestSuite.class);

	public abstract void executeTest(TestResult testResult);

	public void run() {
		logger.info("TestSuite: Called from thread");
		executeTest(testResult);
	}

	public TestResult getTestResult() {
		return testResult;
	}

	public void setTestResult(TestResult testResult) {
		this.testResult = testResult;
	}

	public abstract TestResult getTestResult(String testCaseName);

}
