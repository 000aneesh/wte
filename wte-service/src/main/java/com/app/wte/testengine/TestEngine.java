package com.app.wte.testengine;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.app.wte.model.ExecutionResult;
import com.app.wte.testsuite.TestSuite;
import com.app.wte.util.TestResultComponent;
import com.app.wte.model.ExecutionContext;

@Component
public class TestEngine {
	private static final Logger logger = LoggerFactory.getLogger(TestEngine.class);
	
	@Autowired
	private TaskExecutor taskExecutor;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired
	TestResultComponent testResultComponent;
	
	@Value("${upload-path}")
	private String uploadPath;

	Map<String, TestSuite> testplan = new HashMap<String, TestSuite>();

	/*public void createTestSuite(ExecutionResult testResult) {

		if (!testplan.containsKey(testResult.getTestCase())) {
			logger.info(testResult.getTestCase() + ": test suite is going to start");
			startTestSuite(testResult);
		} else {
			logger.info(testResult.getTestCase() + ": test suite is already running");
		}

	}*/

	public void startTestSuite(String testCase, String fileName, String templateKey, String resultFolderName) {
		TestSuite testSuite = applicationContext.getBean(TestSuite.class);
		testplan.put(testCase, testSuite);
		
		ExecutionContext executionContext=new ExecutionContext();
		executionContext.setTestCase(testCase);
		executionContext.setFileName(fileName);
		executionContext.setTemplateKey(templateKey);
		executionContext.setResultFolderName(resultFolderName);
		//executionContext.setResultFolderName(uploadPath+File.separator+resultFolderName);
		
		testSuite.setExecutionContext(executionContext);
		testResultComponent.getDirectoryNames().add(testCase);
		
		taskExecutor.execute(testplan.get(testCase));
	}
	
	public ExecutionContext getTestResult(String testName){
		if(testplan.get(testName)!= null){
			ExecutionContext executionContext=testplan.get(testName).getExecutionContext();
			
			return executionContext;
		}
		return null;
		
	}
	@Bean(name="processExecutor")
    public TaskExecutor workExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setThreadNamePrefix("Async-");
        threadPoolTaskExecutor.setCorePoolSize(3);
        threadPoolTaskExecutor.setMaxPoolSize(3);
        threadPoolTaskExecutor.setQueueCapacity(600);
        threadPoolTaskExecutor.afterPropertiesSet();
        logger.info("ThreadPoolTaskExecutor set");
        return threadPoolTaskExecutor;
    }

	public void createTestSuite(String testCase, String fileName, String templateKey, String resultFolderName) {
		if (!testplan.containsKey(testCase)) {
			logger.info(testCase + ": test suite is going to start");
			startTestSuite(testCase,fileName,templateKey,resultFolderName);
		} else {
			logger.info(testCase+ ": test suite is already running");
		}
		
	}
}
