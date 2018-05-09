package com.app.wte.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.xmlbeans.impl.jam.internal.DirectoryScanner;
import org.apache.xmlbeans.impl.jam.provider.JamLogger;
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
public class TestResultComponent {
	private static final Logger logger = LoggerFactory.getLogger(TestResultComponent.class);
	
	@Value("${upload-path}")
	private String uploadPath;
	
	List<String> directoryNames=new ArrayList<String>();

	@PostConstruct
	public void initialliize() throws IOException {
		
		File[] directories = new File(uploadPath).listFiles(File::isDirectory);
		for (File dir : directories) {
			directoryNames.add(dir.getName());
		}

		
	}

	public List<String> getDirectoryNames() {
		return directoryNames;
	}

	public void setDirectoryNames(List<String> directoryNames) {
		this.directoryNames = directoryNames;
	}

}
