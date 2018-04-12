/**
 * 
 */
package com.app.wte.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
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

import com.app.wte.model.CsvDTO;
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

//	 @Value("${excel.file.path}")
	private String excelPath;

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

	 @GetMapping(path = "/getTemplateDetails")
	 public ResponseEntity<List<CsvDTO>> templateDeatils(@RequestParam("msg")
	 String msg) throws InvalidFormatException, IllegalStateException, IOException
	 {
	
	 List<CsvDTO> list = ExcelToObject.excelStudentReader(excelPath);
	 return new ResponseEntity<>(list, HttpStatus.OK);
	 }

	
	@PostMapping("/upload")
	public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
		String message = "";
		try {
			storageService.store(file);
			files.add(file.getOriginalFilename());

			message = "You successfully uploaded " + file.getOriginalFilename() + "!";
			return ResponseEntity.status(HttpStatus.OK).body(message);
		} catch (Exception e) {
			message = "FAIL to upload " + file.getOriginalFilename() + "!";
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
		}
	}
	
	@PostMapping(path = "/uploadFile")
	public String upload(@RequestParam("file") MultipartFile file) throws IOException {
		String status = "";
		if (!file.isEmpty()) {
			byte[] bytes = file.getBytes();
			Path path = Paths.get("D://files//" + file.getOriginalFilename());
			Files.write(path, bytes);
			status = "File uploaded successfully";
		} else {
			status = "Please select a file to upload";
		}
		return status;
	}
	
	@GetMapping("/getallfiles")
	public ResponseEntity<List<String>> getListFiles(Model model) {
		List<String> fileNames = files
				.stream().map(fileName -> MvcUriComponentsBuilder
						.fromMethodName(SpringController.class, "getFile", fileName).build().toString())
				.collect(Collectors.toList());

		return ResponseEntity.ok().body(fileNames);
	}

	@GetMapping("/files/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> getFile(@PathVariable String filename) {
		Resource file = storageService.loadFile(filename);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}
	
}
