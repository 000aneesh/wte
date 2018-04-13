package com.app.wte.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class StorageService {

	private Path tempLocation = WTEUtils.geTempPath();

	Logger log = LoggerFactory.getLogger(this.getClass().getName());

	// @Autowired
	// public StorageService(@Value("${tempLocation}") String uploadDir) {
	// rootLocation = Paths.get(uploadDir);
	// }

	public String store(MultipartFile file) {
		try {
			Path path = this.tempLocation.resolve(file.getOriginalFilename());
			String filePath = path.toString();
			Files.copy(file.getInputStream(), this.tempLocation.resolve(file.getOriginalFilename()));
			return filePath;
		} catch (Exception e) {
			throw new RuntimeException("FAIL!");
		}
	}

	public Resource loadFile(String filename) {
		try {
			Path file = tempLocation.resolve(filename);
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
		if (!Files.exists(tempLocation)) {
			try {
				Files.createDirectories(tempLocation);
			} catch (IOException e) {
				throw new RuntimeException("Could not initialize storage!");
			}
		}
	}
}
