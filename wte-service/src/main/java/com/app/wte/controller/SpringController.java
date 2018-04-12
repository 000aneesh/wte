/**
 * 
 */
package com.app.wte.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.app.wte.model.CsvDTO;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author 419312
 *
 */
@RestController
public class SpringController {
	
//	@Value("${excel.file.path}")
	private String excelPath;

	@ApiOperation(value = "Echo message")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 500, message = "Server error") })
	@GetMapping(path = "/echo")
	public String echo(@RequestParam("msg") String msg) {

		return "Hello "+ msg;
	}
	
	@GetMapping(path = "/getTemplateDetails")
	public ResponseEntity<List<CsvDTO>> templateDeatils(@RequestParam("msg") String msg) throws InvalidFormatException, IllegalStateException, IOException {

		List<CsvDTO> list = ExcelToObject.excelStudentReader(excelPath);
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
	
	@PostMapping(path = "/uploadFile")	  
	 public String uploadFileHandler(@RequestParam("file") MultipartFile file) throws IOException {
		 String status="";
	        if (!file.isEmpty()) {
	        	 byte[] bytes = file.getBytes();
	        	 Path path = Paths.get("D://files//" + file.getOriginalFilename());
	        	 Files.write(path , bytes);
	        	 status="File uploaded successfully";
	        }else{
	        	status="Please select a file to upload";
	        }
	        return status;
	 }
}
