package com.app.wte.controller;

import java.io.IOException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.wte.util.AppProperties;


@RestController
public class ConfigurationController {
	private static final Logger logger = LoggerFactory.getLogger(ConfigurationController.class);
	   private AppProperties app;
	    
	   
	    @Autowired
	public void setApp(AppProperties app) {
		this.app = app;
	}
	    private ResourceLoader resourceLoader;
    @Autowired
    public ConfigurationController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    Resource[] loadResources(String pattern) throws IOException {
        return ResourcePatternUtils.getResourcePatternResolver(resourceLoader).getResources(pattern);
    }
	
    @RequestMapping("/request")
	public ArrayList<String> getList() throws IOException {
		ArrayList<String> results = new ArrayList<String>();
		ConfigurationController configurationController = new ConfigurationController(resourceLoader);
		Resource[] resources = configurationController.loadResources(app.getFilePath());
        if (!ObjectUtils.isEmpty(resources)) {
  			for (Resource resource : resources) {
  				if (resource.exists()) {
  					String value = resource.getURL().toString();
  					
  					logger.debug("resource.getFilename()->", resource.getFilename());
  					logger.debug("value->", value);
  					 results.add(resource.getFilename());
  					
  				}
  			}
  		}
		
		return results;
	}
	
		
}

