package com.app.wte.step;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.app.wte.model.ExecutionContext;
import com.app.wte.type.ExecutionStatusType;
import com.app.wte.type.ExecutionStepType;
import com.app.wte.util.ConfigurationComponent;
import com.app.wte.util.WTEUtils;

@Service(value="fileGenerationStep")
public class FileGenerationStep implements TestExecutionStep {
	
	@Value("${upload-path}")
	public String uploadPath;
	
	@Value("${templatePath}")
	private String templatePath;
	
	ExecutionStepType executionTaskType=ExecutionStepType.FileGeneration;
	
	@Autowired
	ConfigurationComponent confComponent;
	
	@Override
	public void execute(ExecutionContext executionContext) {
		try {
			Map<String, String> configData = null;
			if(confComponent.getConfigDataMap() != null){
				configData = confComponent.getConfigDataMap().get(executionContext.getTemplateKey());
				executionContext.setConfigDataMap(configData);
			}
			WTEUtils.readFromExcel(executionContext,uploadPath,templatePath);
			
		} catch (FileNotFoundException e) {
			 WTEUtils.updateStatus(executionContext, this.executionTaskType, ExecutionStatusType.ERROR);
	         WTEUtils.jaxbObjectToXML(executionContext,uploadPath, "");
			e.printStackTrace();
		} catch (IOException e) {
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
		// WTEUtils.jaxbObjectToXML(executionContext,uploadPath,"");
		
	}

	@Override
	public void postProcess(ExecutionContext executionContext) {
		if(WTEUtils.getStatus(executionContext, executionTaskType)!=ExecutionStatusType.ERROR){
			WTEUtils.updateStatus(executionContext, this.executionTaskType, ExecutionStatusType.COMPLETED);
			WTEUtils.jaxbObjectToXML(executionContext,uploadPath, "");	
		}
		
	}
	
}
