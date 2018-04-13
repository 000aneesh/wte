package com.app.wte.serviceImpl;

import org.springframework.stereotype.Service;

import com.app.wte.model.TestRunRequest;
import com.app.wte.service.Task;

@Service(value="fileUploadTask")
public class FileUploadTask implements Task {

	@Override
	public void execute(TestRunRequest testRunRequest) {
		
		
	}

}
