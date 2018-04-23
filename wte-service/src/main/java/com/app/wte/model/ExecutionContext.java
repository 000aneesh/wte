package com.app.wte.model;

import java.util.LinkedHashMap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.app.wte.type.ExecutionStatusType;
import com.app.wte.type.ExecutionStepType;

@XmlRootElement(name="ExecutionStatus")
@XmlAccessorType(XmlAccessType.FIELD)
public class ExecutionContext {
	
		ExecutionResult executionResult;
		
		String testCase;
		
		String fileName;
		
		String templateKey;
		
		String resultFolderName;

		LinkedHashMap<ExecutionStepType,ExecutionStatusType> taskSatusMap = new LinkedHashMap<ExecutionStepType,ExecutionStatusType>();
		
		LinkedHashMap<ExecutionStepType,ProcessValidationResult> taskSatusDetailsMap = new LinkedHashMap<ExecutionStepType,ProcessValidationResult>();
		
		public LinkedHashMap<ExecutionStepType,ExecutionStatusType> getTaskSatusMap() {
			return taskSatusMap;
		}

		public void setTaskSatusMap(LinkedHashMap<ExecutionStepType,ExecutionStatusType> taskSatusMap) {
			this.taskSatusMap = taskSatusMap;
		}

		public ExecutionResult getExecutionResult() {
			return executionResult;
		}
		
		public void setExecutionResult(ExecutionResult executionResult) {
			this.executionResult = executionResult;
		}

		public String getTestCase() {
			return testCase;
		}

		public void setTestCase(String testCase) {
			this.testCase = testCase;
		}

		public String getFileName() {
			return fileName;
		}

		public void setFileName(String fileName) {
			this.fileName = fileName;
		}

		public String getTemplateKey() {
			return templateKey;
		}

		public void setTemplateKey(String templateKey) {
			this.templateKey = templateKey;
		}

		public String getResultFolderName() {
			return resultFolderName;
		}

		public void setResultFolderName(String resultFolderName) {
			this.resultFolderName = resultFolderName;
		}

		public LinkedHashMap<ExecutionStepType, ProcessValidationResult> getTaskSatusDetailsMap() {
			return taskSatusDetailsMap;
		}

		public void setTaskSatusDetailsMap(LinkedHashMap<ExecutionStepType, ProcessValidationResult> taskSatusDetailsMap) {
			this.taskSatusDetailsMap = taskSatusDetailsMap;
		}
		
}

