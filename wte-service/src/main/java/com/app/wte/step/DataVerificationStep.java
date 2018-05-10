package com.app.wte.step;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.app.wte.dao.DBValidationDao;
import com.app.wte.model.DBValidationResponse;
import com.app.wte.model.ExecutionContext;
import com.app.wte.model.ProcessValidationResult;
import com.app.wte.type.ExecutionStatusType;
import com.app.wte.type.ExecutionStepType;
import com.app.wte.util.ConfigurationComponent;
import com.app.wte.util.WTEUtils;

@Service(value="dataVerificationStep")
public class DataVerificationStep implements TestExecutionStep {

	@Override
	public void preprocess(ExecutionContext executionContext) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void execute(ExecutionContext executionContext) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postProcess(ExecutionContext executionContext) {
		// TODO Auto-generated method stub
		
	}/*
	
	
	@Value("${upload-path}")
	public String uploadPath;
	
	
	ExecutionStepType executionStepType=ExecutionStepType.Verification;
	
	private int size = 0;
	
	@Override
	public void execute(ExecutionContext executionContext) {
		
		String inputFile=executionContext.getConfigDataMap().get("templatePattern")+executionContext.getResultFolderName()+".txt";
		
		
			try {
				WTEUtils.mockVerficationResult(executionContext);
	         } catch (Exception e) {
	        	 WTEUtils.updateStatus(executionContext, this.executionStepType, ExecutionStatusType.ERROR);
	        	 WTEUtils.jaxbObjectToXML(executionContext,uploadPath, "");
	 			 e.printStackTrace();
			}
		
			
	}
	
	public ExecutionStepType getExecutionStepType() {
		return executionStepType;
	}

	@Override
	public void preprocess(ExecutionContext executionContext) {
		// TODO 
		
	}

	@Override
	public void postProcess(ExecutionContext executionContext) {
		if(WTEUtils.getStatus(executionContext, executionStepType)!=ExecutionStatusType.ERROR){
			WTEUtils.updateStatus(executionContext, this.executionStepType, ExecutionStatusType.COMPLETED);
			WTEUtils.jaxbObjectToXML(executionContext,uploadPath, "");	
		}
		
	}
*/}
