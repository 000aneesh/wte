package com.app.wte.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.app.wte.type.ExecutionStatusType;
import com.app.wte.type.ExecutionStepType;

@XmlRootElement(name="ExecutionStatus")
@XmlAccessorType(XmlAccessType.FIELD)
public class ExecutionContext {		
		
		String testCase;
		
		String fileName;
		
		String templateKey;
		
		String resultFolderName;
		
		Map<String, String> configDataMap=new HashMap<String, String>();

		LinkedHashMap<ExecutionStepType,ExecutionStatusType> taskSatusMap = new LinkedHashMap<ExecutionStepType,ExecutionStatusType>();
		
		LinkedHashMap<ExecutionStepType,ProcessValidationResult> taskSatusDetailsMap = new LinkedHashMap<ExecutionStepType,ProcessValidationResult>();
		
		List<TestRecord> testRecordList = new ArrayList<TestRecord>();
		
		public LinkedHashMap<ExecutionStepType,ExecutionStatusType> getTaskSatusMap() {
			return taskSatusMap;
		}

		public void setTaskSatusMap(LinkedHashMap<ExecutionStepType,ExecutionStatusType> taskSatusMap) {
			this.taskSatusMap = taskSatusMap;
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
		
		public Map<String, String> getConfigDataMap() {
			return configDataMap;
		}

		public void setConfigDataMap(Map<String, String> configDataMap) {
			this.configDataMap = configDataMap;
		}

		public LinkedHashMap<ExecutionStepType, ProcessValidationResult> getTaskSatusDetailsMap() {
			return taskSatusDetailsMap;
		}

		public void setTaskSatusDetailsMap(LinkedHashMap<ExecutionStepType, ProcessValidationResult> taskSatusDetailsMap) {
			this.taskSatusDetailsMap = taskSatusDetailsMap;
		}

		public List<TestRecord> getTestRecordList() {
			return testRecordList;
		}

		public void setTestRecordList(List<TestRecord> testRecordList) {
			this.testRecordList = testRecordList;
		}

}

