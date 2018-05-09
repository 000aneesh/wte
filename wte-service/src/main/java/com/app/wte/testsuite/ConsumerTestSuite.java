package com.app.wte.testsuite;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.app.wte.model.ExecutionContext;
import com.app.wte.step.TestExecutionStep;

@Component
@Scope("prototype")
public class ConsumerTestSuite extends TestSuite {

	
	private static final Logger logger = LoggerFactory.getLogger(ConsumerTestSuite.class);
	List<TestExecutionStep> taskList = new ArrayList<TestExecutionStep>();

	@PostConstruct
	public void initialize(){
		taskList.add(fileGenerationStep);
		taskList.add(fTPTransferStep);
		taskList.add(dBValidationStep);
		taskList.add(dataVerificationStep);
	}
	
	@Override
	public void executeTest(ExecutionContext executionContext) {
		
		logger.info("ConsumerTestSuite:executeTest Called from thread");
		for(TestExecutionStep task:taskList){
			task.preprocess(executionContext);
			task.execute(executionContext);	
			task.postProcess(executionContext);
		}
		
	}
	
}
