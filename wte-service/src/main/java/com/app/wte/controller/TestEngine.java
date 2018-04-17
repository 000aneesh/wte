package com.app.wte.controller;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.wte.model.TestResult;
import com.app.wte.testengine.TestSuite;

@RestController
public class TestEngine {
	private static final Logger logger = LoggerFactory.getLogger(TestEngine.class);
	
	@Value("${upload-path}")
	private String uploadPath;
	
	@Autowired
	private TaskExecutor taskExecutor;

	@Autowired
	private ApplicationContext applicationContext;

	Map<String, TestSuite> testplan = new HashMap<>();

	@RequestMapping(value = "/testRun")
	public void createTestSuite(@RequestBody TestResult testResult) {
		startTestSuite(testResult);
	}

	public void startTestSuite(TestResult testResult) {
		testResult.setResultFolderName(uploadPath + File.separator + testResult.getResultFolderName());
		System.out.println("entry to test engine");
		logger.info("entry to test engine");
		TestSuite myThread = applicationContext.getBean(TestSuite.class);
		myThread.setTestResult(testResult);
		taskExecutor.execute((Runnable) myThread);
		if (myThread.getTestResult() != null) {
			logger.info("TestEngine getTaskSatusMap FileGenerationTask: "
					+ myThread.getTestResult().getTaskSatusMap().get("FileGenerationTask"));
			logger.info("TestEngine getTaskSatusMap FTPTransferTask: "
					+ myThread.getTestResult().getTaskSatusMap().get("FTPTransferTask"));
		}
		logger.info("exit from test engine");
		System.out.println("exit from test engine");
	}

	@Bean(name = "processExecutor")
	public TaskExecutor workExecutor() {
		ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
		threadPoolTaskExecutor.setThreadNamePrefix("Test Engine Async-");
		threadPoolTaskExecutor.setCorePoolSize(3);
		threadPoolTaskExecutor.setMaxPoolSize(3);
		threadPoolTaskExecutor.setQueueCapacity(600);
		threadPoolTaskExecutor.afterPropertiesSet();
		logger.info("ThreadPoolTaskExecutor set");
		return threadPoolTaskExecutor;
	}
}
