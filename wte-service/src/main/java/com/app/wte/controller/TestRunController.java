package com.app.wte.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.app.wte.model.ExecutionContext;
import com.app.wte.model.FileUploadResponse;
import com.app.wte.model.ProcessValidationResult;
import com.app.wte.testengine.TestEngine;
import com.app.wte.type.ExecutionStatusType;
import com.app.wte.type.ExecutionStepType;
import com.app.wte.util.WTEUtils;

@RestController
public class TestRunController {
	
	
	@Autowired
	WTEUtils wTEUtils;
	
	@Autowired
	TestEngine testEngine;

	@RequestMapping(value="/testRun")	
	public void testCaseRun(@RequestParam("testCase") String testCase,
			@RequestParam("fileName") String fileName,
			@RequestParam("templateKey") String templateKey,
			@RequestParam("resultFolderName") String resultFolderName){		
		
		
		/*testResult.setTestCase("TestCase01");
		testResult.setFileName("TestData.xlsx");*/
		//wTEUtils.createResultsFolder(resultFolderName);
		
		testEngine.createTestSuite(testCase,fileName,templateKey,resultFolderName);
		 
	}
	
	@RequestMapping(value="/testExecute")	
	public String testExecute(){		
				
		String testCase="TestCase01";
		String fileName="TestData.xlsx";
		String templateKey="";
		String resultFolderName="Run-20180417170132814";
		
		testEngine.createTestSuite(testCase,fileName,templateKey,resultFolderName);
		System.out.println("Exit testExecute");
		 
		return "testRunSuccess";
	}
	
	@RequestMapping(value="/getResult")	
	public ExecutionStatusType getResult(@RequestParam("testCase") String testCase,@RequestParam("executionStep") String executionStep){	
		System.out.println("Entered getResult method: ");
		//ExecutionContext executionContext=testEngine.getTestResult("TestCase01");
		
		ExecutionContext executionContext=testEngine.getTestResult(testCase);
		ExecutionStatusType executionStatusType = WTEUtils.getExecutionSatus(executionContext, executionStep);
		
		System.out.println("getResult method TestCase name: "+executionContext.getTestCase()+"getFileName: "+executionContext.getFileName());
		return executionStatusType;
	}
	@RequestMapping(value="/getResultDetails")	
	public ProcessValidationResult getResultDetails(@RequestParam("testCase") String testCase,@RequestParam("executionStep") String executionStep){	
		System.out.println("Entered getResult method: ");
		//ExecutionContext executionContext=testEngine.getTestResult("TestCase01");
		
		ExecutionContext executionContext=testEngine.getTestResult(testCase);
		ExecutionStepType executionStepType = WTEUtils.getExecutionStep(executionContext, executionStep);
		
		System.out.println("getResult method TestCase name: "+executionContext.getTestCase()+"getFileName: "+executionContext.getFileName());
		return executionContext.getTaskSatusDetailsMap().get(executionStepType);
	}
	 
	
}
