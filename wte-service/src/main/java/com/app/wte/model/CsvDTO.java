package com.app.wte.model;

import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "data")
public class CsvDTO {
	private Map<String, DataDTO> key;

	public Map<String, DataDTO> getKey() {
		return key;
	}

	public void setKey(Map<String, DataDTO> key) {
		this.key = key;
	}

}
