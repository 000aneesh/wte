package com.app.wte.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.app.wte.model.ExecutionContext;
import com.app.wte.model.FileUploadResponse;
import com.app.wte.model.ProcessValidationResult;
import com.app.wte.model.TestRecord;
import com.app.wte.testengine.TestEngine;
import com.app.wte.type.ExecutionStatusType;
import com.app.wte.type.ExecutionStepType;
import com.app.wte.util.WTEUtils;

@RestController
public class TestRunController {
	
	
	
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
		String templateKey="template 1";
		String resultFolderName="Run-20180417170132814";
		
		testEngine.createTestSuite(testCase,fileName,templateKey,resultFolderName);
		//WTEUtils wTEUtils=new WTEUtils();
		//wTEUtils.createResultsFolder();
		System.out.println("Exit testExecute");
		 
		return "testRunSuccess";
	}
	
	@RequestMapping(value="/getResult")	
	@ResponseBody
	public ExecutionStatusType getResult(@RequestParam("testCase") String testCase,@RequestParam("executionStep") String executionStep){	
		
		ExecutionStatusType executionStatusType =null;
		
		ExecutionContext executionContext=testEngine.getTestResult(testCase);
		if(executionContext != null){
			executionStatusType = WTEUtils.getExecutionSatus(executionContext, executionStep);
		}
		
		return executionStatusType;
	}
	@RequestMapping(value="/getResultDetails")	
	@ResponseBody
	public ProcessValidationResult getResultDetails(@RequestParam("testCase") String testCase,@RequestParam("executionStep") String executionStep){	
		
		ProcessValidationResult processValidationResult=null;
		
		ExecutionContext executionContext=testEngine.getTestResult(testCase);
		if(executionContext != null && executionContext.getTaskSatusDetailsMap() != null){
			ExecutionStepType executionStepType = WTEUtils.getExecutionStep(executionStep);
			processValidationResult = executionContext.getTaskSatusDetailsMap().get(executionStepType);
		}
		
		return processValidationResult;
	}
	@RequestMapping(value="/getTestRecords")	
	public List getTestRecords(@RequestParam("testCase") String testCase){	
		System.out.println("Entered getTestRecords method: ");
		
		List<TestRecord> testRecords=new ArrayList<TestRecord>();
		
		ExecutionContext executionContext=testEngine.getTestResult(testCase);
		if(executionContext != null && executionContext.getTestRecordList() != null){
			testRecords = executionContext.getTestRecordList();
		}
		
		return testRecords;
	}
	 
	
}
