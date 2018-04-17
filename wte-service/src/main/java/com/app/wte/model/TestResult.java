package com.app.wte.model;

import java.util.LinkedHashMap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="TestResult")
@XmlAccessorType(XmlAccessType.FIELD)
public class TestResult {

	String testCase;
	
	String fileName;
	
	String templateKey;
	
	String resultFolderName;
	
	LinkedHashMap<String,String> taskSatusMap = new LinkedHashMap<String,String>();
	
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

	public String getResultFolderName() {
		return resultFolderName;
	}

	public void setResultFolderName(String resultFolderName) {
		this.resultFolderName = resultFolderName;
	}

	public void setTemplateKey(String templateKey) {
		this.templateKey = templateKey;
	}

	public LinkedHashMap<String, String> getTaskSatusMap() {
		return taskSatusMap;
	}

	public void setTaskSatusMap(LinkedHashMap<String, String> taskSatusMap) {
		this.taskSatusMap = taskSatusMap;
	}
	
}
