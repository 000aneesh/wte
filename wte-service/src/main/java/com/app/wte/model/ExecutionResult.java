package com.app.wte.model;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="ExecutionResult")
@XmlAccessorType(XmlAccessType.FIELD)
public class ExecutionResult {
	
	Map<String,TestRecord> testRecordMap = new LinkedHashMap<String,TestRecord>();

	
	public Map<String, TestRecord> getTestRecordMap() {
		return testRecordMap;
	}

	public void setTestRecordMap(Map<String, TestRecord> testRecordMap) {
		this.testRecordMap = testRecordMap;
	}

}
