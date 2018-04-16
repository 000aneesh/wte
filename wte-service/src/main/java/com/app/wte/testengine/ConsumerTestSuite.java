package com.app.wte.testengine;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.app.wte.model.TestResult;
import com.app.wte.service.Task;

@Component
@Scope("prototype")
public class ConsumerTestSuite extends TestSuite {

	private static final Logger logger = LoggerFactory.getLogger(ConsumerTestSuite.class);
	List<Task> taskList = new ArrayList<>();

	public void initialize() {
		taskList.add(fileGenerationTask);
		taskList.add(fTPTransferTask);
	}

	@Override
	public void executeTest(TestResult testResult) {
		initialize();
		logger.info("ConsumerTestSuite:executeTest Called from thread");
		for (Task task : taskList) {
			task.execute(testResult);
		}
		if (testResult.getTaskSatusMap() != null) {
			logger.info("ConsumerTestSuite getTaskSatusMap FileGenerationTask: "
					+ testResult.getTaskSatusMap().get("FileGenerationTask"));
			logger.info("ConsumerTestSuite getTaskSatusMap FTPTransferTask: "
					+ testResult.getTaskSatusMap().get("FTPTransferTask"));
		}
	}

	@RequestMapping(value = "getResult/{testName}", method = RequestMethod.GET)
	public String getTestData(String testName) {
		// TODO Auto-generated method stub
		return testName;
	}

	@Override
	public TestResult getTestResult(String testCaseName) {
		// TODO Auto-generated method stub
		return null;
	}

}
