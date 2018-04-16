package com.app.wte.util;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.app.wte.constants.WTEConstants;

@Component
public class StorageService {

	// private Path tempLocation = WTEUtils.getTempPath();

	Logger log = LoggerFactory.getLogger(this.getClass().getName());

	// @Autowired
	// public StorageService(@Value("${tempLocation}") String uploadDir) {
	// rootLocation = Paths.get(uploadDir);
	// }

	public String store(MultipartFile file) {
		String uploadPath = WTEUtils.getUploadPath();
		long uniqueTimeStamp = WTEUtils.getUniqueTimeStamp();
		Path fileLocation = Paths.get(uploadPath + uniqueTimeStamp);
		try {

			if (!Files.exists(fileLocation)) {
				try {
					Files.createDirectories(fileLocation);
				} catch (IOException e) {
					throw new RuntimeException("Could not initialize storage!");
				}
			}

			Path path = fileLocation.resolve(file.getOriginalFilename());
			Files.copy(file.getInputStream(), path);
			//String filePath = path.toString();
			return String.valueOf(uniqueTimeStamp);
		} catch (Exception e) {
			throw new RuntimeException("FAIL!");
		}
	}

	public Resource loadFile(String filePath) {
		try {
			Path file = Paths.get(WTEUtils.getUploadPath() + filePath);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new RuntimeException("FAIL!");
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException("FAIL!");
		}
	}

	// public void deleteAll() {
	// FileSystemUtils.deleteRecursively(rootLocation.toFile());
	// }

	public void init() {
		Path extPropertiesLocation = Paths.get(WTEUtils.BASE_PATH + WTEConstants.EXT_PROPERTY_FOLDER + File.separator + WTEConstants.EXT_PROPERTY_FILE);
		if (!Files.exists(extPropertiesLocation)) {
			throw new RuntimeException("wte.properties not found in specified location !");
		}
//		if (!Files.exists(tempLocation)) {
//			try {
//				Files.createDirectories(tempLocation);
//			} catch (IOException e) {
//				throw new RuntimeException("Could not initialize storage!");
//			}
//		}
	}
}
