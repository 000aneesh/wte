package com.app.wte.model;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class TestResult {
	Map<String,String> testStatus=new LinkedHashMap<String,String>();

	public Map<String, String> getTestStatus() {
		return testStatus;
	}

	public void setTestStatus(Map<String, String> testStatus) {
		this.testStatus = testStatus;
	}

}
