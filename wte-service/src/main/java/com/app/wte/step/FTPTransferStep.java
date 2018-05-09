package com.app.wte.step;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.app.wte.model.ExecutionContext;
import com.app.wte.type.ExecutionStatusType;
import com.app.wte.type.ExecutionStepType;
import com.app.wte.util.ConfigurationComponent;
import com.app.wte.util.WTEUtils;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

@Service(value="fTPTransferStep")
public class FTPTransferStep implements TestExecutionStep {
	
	
	@Value("${upload-path}")
	private String uploadPath;
	
	@Value("${ftpFilePath}")
	private String ftpFilePath;
	
	@Value("${host}")
	private String host;
	
	@Value("${port}")
	private String port;
	
	@Value("${ftp.user}")
	private String userName;
	
	@Value("${password}")
	private String password;
	
	ExecutionStepType executionTaskType=ExecutionStepType.FTPTransfer;
	

	@Override
	public void execute(ExecutionContext executionContext) {
		try {
			
			 File sourceFile = new File(uploadPath + File.separator +executionContext.getTestCase()+File.separator+"TestData"
			 			+File.separator+executionContext.getConfigDataMap().get("templatePattern")+executionContext.getResultFolderName()+".txt");
			 File destFile = new File(ftpFilePath+"/"+executionContext.getConfigDataMap().get("templatePattern")+executionContext.getResultFolderName()+".txt");
			 Files.copy(sourceFile.toPath(), destFile.toPath());
			//WTEUtils.copyToServer(executionContext,uploadPath,ftpFilePath,host,port,userName,password);
			
	         } /*catch (JSchException e) {
	 			 WTEUtils.updateStatus(executionContext, this.executionTaskType, ExecutionStatusType.ERROR);
	 			 WTEUtils.jaxbObjectToXML(executionContext,uploadPath, "");
	 			 e.printStackTrace();
			} catch (SftpException e) {
				 WTEUtils.updateStatus(executionContext, this.executionTaskType, ExecutionStatusType.ERROR);
	        	WTEUtils.jaxbObjectToXML(executionContext,uploadPath, "");
	 			e.printStackTrace();
				e.printStackTrace();
			} */catch (IOException e) {
				WTEUtils.updateStatus(executionContext, this.executionTaskType, ExecutionStatusType.ERROR);
				WTEUtils.jaxbObjectToXML(executionContext,uploadPath, "");	
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
		 //WTEUtils.jaxbObjectToXML(executionContext,uploadPath, "");	
		
	}

	@Override
	public void postProcess(ExecutionContext executionContext) {
		if(WTEUtils.getStatus(executionContext, executionTaskType)!=ExecutionStatusType.ERROR){
			WTEUtils.updateStatus(executionContext, this.executionTaskType, ExecutionStatusType.COMPLETED);
			WTEUtils.jaxbObjectToXML(executionContext,uploadPath, "");	
		}
		
	}
	
}
