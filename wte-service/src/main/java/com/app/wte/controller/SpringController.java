/**
 * 
 */
package com.app.wte.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.app.wte.model.CsvDTO;
import com.app.wte.model.FileUploadResponse;
import com.app.wte.model.TestRunRequest;
import com.app.wte.service.Task;
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

	// @Value("${excel.file.path}")
	private String excelPath;

	@Value("${tempLocation}")
	private String newLoc;

	@Autowired
	@Qualifier("fileGenerationTask")
	Task fileGenerationTask;

	@Autowired
	@Qualifier("fTPTransferTask")
	Task fTPTransferTask;

	@Autowired
	@Qualifier("fileUploadTask")
	Task fileUploadTask;

	@Value("${tempLocation}")
	private String tempLocation;

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
	public ResponseEntity<List<CsvDTO>> templateDeatils(@RequestParam("msg") String msg)
			throws InvalidFormatException, IllegalStateException, IOException {

		List<CsvDTO> list = ExcelToObject.excelStudentReader(excelPath);
		return new ResponseEntity<>(list, HttpStatus.OK);
	}

	@PostMapping("/upload")
	public ResponseEntity<FileUploadResponse> handleFileUpload(@RequestParam("file") MultipartFile file)
			throws URISyntaxException, IOException {
		String message = "";

		FileUploadResponse fileUploadResponse = new FileUploadResponse();
		try {
			storageService.store(file);
			files.add(file.getOriginalFilename());

			message = "You successfully uploaded " + file.getOriginalFilename() + "!";

			fileUploadResponse.setFilename(file.getOriginalFilename());
			fileUploadResponse.setStatusMsg(message);

			return new ResponseEntity<>(fileUploadResponse, HttpStatus.OK);
		} catch (Exception e) {
			message = "FAIL to upload " + file.getOriginalFilename() + "!";
			fileUploadResponse.setStatusMsg(message);
			return new ResponseEntity<>(fileUploadResponse, HttpStatus.EXPECTATION_FAILED);
		}
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

	@RequestMapping(value = "/testRun")
	public String getMessage(@RequestBody TestRunRequest testRunRequest) {

		System.out.println("getFileName : " + testRunRequest.getFileName());

		fileGenerationTask.execute(testRunRequest);

		fTPTransferTask.execute(testRunRequest);

		return "success";
	}

	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	@ResponseBody
	public FileUploadResponse uploadFileHandler(@RequestParam("file") MultipartFile file) throws IOException {

		String status = "";
		InputStream in = null;
		OutputStream out = null;
		String filename = "";
		FileUploadResponse fileUploadResponse = new FileUploadResponse();
		if (!file.isEmpty()) {
			File dir = new File(tempLocation);
			if (!dir.exists())
				dir.mkdirs();
			filename = file.getOriginalFilename();
			File serverFile = new File(dir.getAbsolutePath() + File.separator + file.getOriginalFilename());
			in = file.getInputStream();
			out = new FileOutputStream(serverFile);
			byte[] b = new byte[1024];
			int len = 0;
			while ((len = in.read(b)) > 0) {
				out.write(b, 0, len);
			}
			out.close();
			in.close();

			status = "File uploaded successfully";
		} else {
			status = "Please select a file to upload";
		}
		fileUploadResponse.setFilename(filename);
		fileUploadResponse.setStatusMsg(status);
		return fileUploadResponse;
	}

}
