/**
 * 
 */
package com.app.wte.controller;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.app.wte.model.FileUploadResponse;
import com.app.wte.util.ConfigurationComponent;
import com.app.wte.util.StorageService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author 419312
 *
 */
@RestController
public class SpringController {

	@Autowired
	ConfigurationComponent confComponent;

	@Autowired
	StorageService storageService;

	List<String> files = new ArrayList<String>();

	@ApiOperation(value = "Echo message")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 500, message = "Server error") })
	@GetMapping(path = "/echo")
	public String echo(@RequestParam("msg") String msg) {

		return "Hello " + msg;
	}

	@GetMapping(path = "/getTemplates")
	public ResponseEntity<Set<String>> templateDeatils()
			throws InvalidFormatException, IllegalStateException, IOException {

		Set<String> list = confComponent.getTemplates();
		return new ResponseEntity<>(list, HttpStatus.OK);
	}

	@PostMapping("/upload")
	public ResponseEntity<FileUploadResponse> handleFileUpload(@RequestParam("file") MultipartFile file)
			throws URISyntaxException, IOException {
		String message = "";

		FileUploadResponse fileUploadResponse = new FileUploadResponse();
		try {
			String fileLocation = storageService.store(file);
			files.add(file.getOriginalFilename());

			message = "You successfully uploaded " + file.getOriginalFilename() + "!";

			fileUploadResponse.setFileLocation(fileLocation);
			fileUploadResponse.setFileName(file.getOriginalFilename());
			fileUploadResponse.setStatus(message);

			return new ResponseEntity<>(fileUploadResponse, HttpStatus.OK);
		} catch (Exception e) {
			message = "FAIL to upload " + file.getOriginalFilename() + "!";
			fileUploadResponse.setStatus(message);
			return new ResponseEntity<>(fileUploadResponse, HttpStatus.EXPECTATION_FAILED);
		}
	}

	@GetMapping("/getAllFiles")
	public ResponseEntity<List<String>> getListFiles(Model model) {
		List<String> fileNames = files
				.stream().map(fileName -> MvcUriComponentsBuilder
						.fromMethodName(SpringController.class, "getFile", fileName).build().toString())
				.collect(Collectors.toList());

		return ResponseEntity.ok().body(fileNames);
	}

	@GetMapping("/files/{filePath}/{fileName:.+}")
	@ResponseBody
	public ResponseEntity<Resource> getFile(@PathVariable String filePath, @PathVariable String fileName)
			throws IOException {
		System.out.println("file : " + filePath+File.separator+"TestData" + File.separator + fileName);
		Resource file = storageService.loadFile(filePath + File.separator+"TestData" + File.separator + fileName);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}

}
