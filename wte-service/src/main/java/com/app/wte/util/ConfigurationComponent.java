package com.app.wte.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.xmlbeans.impl.jam.internal.DirectoryScanner;
import org.apache.xmlbeans.impl.jam.provider.JamLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	private String rescPath;
	
	@Value("${config.filePath}")
	private String filePath;
	
	Map<String, Map<String, String>> configDataMap= new HashMap<>();
	ArrayList<String> results = new ArrayList<String>();

	// @Autowired
	// public void setApp(AppProperties app) {
	// this.app = app;
	// }

	private ResourceLoader resourceLoader;

	Resource[] loadResources(String pattern) throws IOException {
		return ResourcePatternUtils.getResourcePatternResolver(resourceLoader).getResources(pattern);
	}
	
	public Set<String> getTemplates() throws IOException {
		return configDataMap.keySet();
	}
	
	@PostConstruct
	public void initialliize() throws IOException {
		DirectoryScanner scanner = new DirectoryScanner(new File(filePath), new JamLogger() {
			
			@Override
			public void warning(String arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void warning(Throwable arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void verbose(Throwable arg0, Object arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void verbose(String arg0, Object arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void verbose(Throwable arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void verbose(String arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setVerbose(Class arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setShowWarnings(boolean arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean isVerbose(Class arg0) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean isVerbose(Object arg0) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean isVerbose() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public void error(String arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void error(Throwable arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		scanner.include("*.xlsx");
		String[] files = scanner.getIncludedFiles();
		logger.info("intiallizing...."+Arrays.asList(files));
		
		if (!ObjectUtils.isEmpty(files)) {
			for (String file : files) {
				
				Map<String, String> configData ;
				
					
					logger.info("resource.getFilename()->", file);
					configData=WTEUtils.readFromExcel(filePath, file);
							
					int pos = file.lastIndexOf(".");
					if (pos != -1) {
						results.add(file.substring(0, pos));
						configDataMap.put(file.substring(0, pos), configData);

					

				}
			}
		}
		logger.info("configDataMap->"+configDataMap);
	
	}

	public Map<String, Map<String, String>> getConfigDataMap() {
		return configDataMap;
	}

	public void setConfigDataMap(Map<String, Map<String, String>> configDataMap) {
		this.configDataMap = configDataMap;
	}

}
