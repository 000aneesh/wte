package com.app.wte.step;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.wte.model.ExecutionContext;
import com.app.wte.type.ExecutionStatusType;
import com.app.wte.type.ExecutionStepType;
import com.app.wte.util.WTEUtils;

@Service(value="fileGenerationTask")
public class FileGenerationStep implements TestExecutionStep {
	
	@Autowired
	WTEUtils wTEUtils;
	
	ExecutionStepType executionTaskType=ExecutionStepType.FileGeneration;
	
	@Override
	public void execute(ExecutionContext executionContext) {
		try {
			
			wTEUtils.readFromExcel(executionContext);
			
		//	executionContext.getExecutionResult().getTaskSatusMap().put(Tasks.FileGenerationTask.toString(),TaskStatus.Success.toString());
			//wTEUtils.jaxbObjectToXML(executionContext, "");
			
		} catch (FileNotFoundException e) {
			 WTEUtils.updateStatus(executionContext, this.executionTaskType, ExecutionStatusType.ERROR);
	         wTEUtils.jaxbObjectToXML(executionContext, "");
			e.printStackTrace();
		} catch (IOException e) {
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
