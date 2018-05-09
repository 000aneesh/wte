package com.app.wte.testsuite;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.app.wte.model.ExecutionContext;
import com.app.wte.step.TestExecutionStep;

public abstract class TestSuite implements Runnable {

	@Autowired
	@Qualifier("fileGenerationStep")
	TestExecutionStep fileGenerationStep;

	@Autowired
	@Qualifier("fTPTransferStep")
	TestExecutionStep fTPTransferStep;
	
	@Autowired
	@Qualifier("dBValidationStep")
	TestExecutionStep dBValidationStep;
	
	@Autowired
	@Qualifier("dataVerificationStep")
	TestExecutionStep dataVerificationStep;

	ExecutionContext executionContext;

	
	private static final Logger logger = LoggerFactory.getLogger(TestSuite.class);

	public abstract void executeTest(ExecutionContext executionStatus);

	public void run() {
		logger.info("TestSuite: Called from thread");
		executeTest(executionContext);
	}

	public ExecutionContext getExecutionContext() {
		return executionContext;
	}

	public void setExecutionContext(ExecutionContext executionContext) {
		this.executionContext = executionContext;
	}

}
