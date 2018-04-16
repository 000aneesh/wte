package com.app.wte.model;

import java.util.HashMap;

public class TestResult {

	String testCase;
	
	String fileName;
	
	String templateKey;
	
	HashMap<String,String> taskSatusMap = new HashMap<String, String>();
	

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

	public HashMap<String, String> getTaskSatusMap() {
		return taskSatusMap;
	}

	public void setTaskSatusMap(HashMap<String, String> taskSatusMap) {
		this.taskSatusMap = taskSatusMap;
	}
	
}
