package com.app.wte.step;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.app.wte.model.ExecutionContext;
import com.app.wte.type.ExecutionStatusType;
import com.app.wte.type.ExecutionStepType;
import com.app.wte.util.WTEUtils;

@Service(value="fTPTransferTask")
public class FTPTransferStep implements TestExecutionStep {
	
	
	@Value("${ftpFilePath}")
	private String ftpFilePath;
	
	@Value("${upload-path}")
	private String uploadPath;
	
	ExecutionStepType executionTaskType=ExecutionStepType.FTPTransfer;
	
	@Autowired
	WTEUtils wTEUtils;

	@Override
	public void execute(ExecutionContext executionContext) {
		try {
			 File sourceFile = new File(uploadPath + File.separator +executionContext.getResultFolderName()+File.separator+"TestData"
			 			+File.separator+executionContext.getResultFolderName()+".txt");
			 File destFile = new File(ftpFilePath);
			 Files.copy(sourceFile.toPath(), destFile.toPath());
			//wTEUtils.copyToServer(executionContext);
			
			 wTEUtils.jaxbObjectToXML(executionContext, "");
	         }/* catch (JSchException e) {
	 			 WTEUtils.updateStatus(executionContext, this.executionTaskType, ExecutionStatusType.ERROR);
	 			 wTEUtils.jaxbObjectToXML(executionContext, "");
	 			 e.printStackTrace();
			} catch (SftpException e) {
				 WTEUtils.updateStatus(executionContext, this.executionTaskType, ExecutionStatusType.ERROR);
	        	wTEUtils.jaxbObjectToXML(executionContext, "");
	 			e.printStackTrace();
				e.printStackTrace();
			}*/ catch (IOException e) {
				WTEUtils.updateStatus(executionContext, this.executionTaskType, ExecutionStatusType.ERROR);
				 wTEUtils.jaxbObjectToXML(executionContext, "");	
				e.printStackTrace();
			}	
		
	}
	
	public ExecutionStepType getExecutionTaskType() {
		return executionTaskType;
	}
	@Override
	public void preprocess(ExecutionContext executionContext) {
		// TODO 
		 WTEUtils.updateStatus(executionContext, this.executionTaskType, ExecutionStatusType.IN_PROGRESS);
		 wTEUtils.jaxbObjectToXML(executionContext, "");	
		
	}

	@Override
	public void postProcess(ExecutionContext executionContext) {
		if(WTEUtils.getStatus(executionContext, executionTaskType)!=ExecutionStatusType.ERROR){
			WTEUtils.updateStatus(executionContext, this.executionTaskType, ExecutionStatusType.COMPLETED);
			wTEUtils.jaxbObjectToXML(executionContext, "");	
		}
		
	}
	
}
