package com.app.wte.model;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class TestRecord {
	
	int testCaseNumber;
	String testCaseDescription;
	Map<String,String> inputTestData=new LinkedHashMap<String,String>();
	Map<String,String> expectedTestData=new LinkedHashMap<String,String>();
	String status;
	TestResult testresult;
	
	public Map<String, String> getInputTestData() {
		return inputTestData;
	}
	public void setInputTestData(Map<String, String> inputTestData) {
		this.inputTestData = inputTestData;
	}
	public Map<String, String> getExpectedTestData() {
		return expectedTestData;
	}
	public void setExpectedTestData(Map<String, String> expectedTestData) {
		this.expectedTestData = expectedTestData;
	}
	public int getTestCaseNumber() {
		return testCaseNumber;
	}
	public void setTestCaseNumber(int testCaseNumber) {
		this.testCaseNumber = testCaseNumber;
	}
	public String getTestCaseDescription() {
		return testCaseDescription;
	}
	public void setTestCaseDescription(String testCaseDescription) {
		this.testCaseDescription = testCaseDescription;
	}
	public TestResult getTestresult() {
		return testresult;
	}
	public void setTestresult(TestResult testresult) {
		this.testresult = testresult;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
