package com.app.wte.util;

import java.io.IOException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
public class ConfigurationComponent {

	private static final Logger logger = LoggerFactory.getLogger(ConfigurationComponent.class);
	// private AppProperties app;

	@Value("${app.filePath}")
	private String filePath;

	private ResourceLoader resourceLoader;

	@Autowired
	public ConfigurationComponent(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	Resource[] loadResources(String pattern) throws IOException {
		return ResourcePatternUtils.getResourcePatternResolver(resourceLoader).getResources(pattern);
	}

	public ArrayList<String> getTemplates() throws IOException {
		ArrayList<String> results = new ArrayList<String>();
		ConfigurationComponent configurationController = new ConfigurationComponent(resourceLoader);
		Resource[] resources = configurationController.loadResources(filePath);
		if (!ObjectUtils.isEmpty(resources)) {
			for (Resource resource : resources) {
				if (resource.exists()) {
					String fileName = resource.getFilename();
					int pos = fileName.lastIndexOf(".");
					if (pos != -1) {
						results.add(fileName.substring(0, pos));
					}

				}
			}
		}

		return results;
	}

}
