package com.app.wte.controller;

import java.io.File;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.wte.util.AppProperties;


@RestController
public class ConfigurationController {
	private AppProperties app;
	
	@RequestMapping("/getList")
	public ArrayList<String> getList() {
		ArrayList<String> results = new ArrayList<String>();
		System.out.println("app.getFilePath(): " + app.getFilePath());
		File[] files = new File(app.getFilePath()).listFiles();

		for (File file : files) {
			if (file.isFile()) {
				 results.add(file.getName());
			} else if (file.isDirectory()) {
				System.out.println("Directory " + file.getName());
			}
		}
		return results;
	}
	
		@Autowired
	    public void setApp(AppProperties app) {
	        this.app = app;
	    }
}
