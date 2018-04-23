package com.app.wte.step;

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
import com.app.wte.util.WTEUtils;

@Service(value="dBValidationTask")
public class DBValidationStep implements TestExecutionStep {
	
	
	@Value("${ftpFilePath}")
	private String ftpFilePath;
	
	@Autowired
	DBValidationDao dBValidationDao;
	
	ExecutionStepType executionTaskType=ExecutionStepType.ProcessValidationEdgeToRaw;
	
	private int size = 0;
	
	@Override
	public void execute(ExecutionContext executionContext) {
		DBValidationResponse dbValidationResponse=null;
		String inputFile=executionContext.getResultFolderName()+".txt";
			try {
				
				/*while(size<=4){
					 dbValidationResponse=dBValidationDao.getStageOfSourceFile(inputFile);
					  if(dbValidationResponse != null && dbValidationResponse.getResultList()!=null) {
						  List<Map<String, Object>> resultList=dbValidationResponse.getResultList();
						  for(Map<String, Object> result:resultList){
							  ProcessValidationResult processValidationResult=new ProcessValidationResult();
							  processValidationResult.setStageName(result.get("STAGE_NAME").toString());
							  processValidationResult.setFileName(result.get("FILE_NAME").toString());
							  
							  executionTaskType = WTEUtils.getExecutionStep(executionContext,result.get("STAGE_NAME").toString());
							  
							  executionContext.getTaskSatusDetailsMap().put(executionTaskType,processValidationResult);				  
							  executionContext.getTaskSatusMap().put(executionTaskType,ExecutionStatusType.COMPLETED);
							  
							  wTEUtils.jaxbObjectToXML(executionContext, "");
						  }
						 
						size=  dbValidationResponse.getResultList().size();
					  }
	                 try {
							Thread.sleep(30000);
	                 } catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
	                 }
	                        
	      	   }*/
				ProcessValidationResult processValidationResult=new ProcessValidationResult();
				  processValidationResult.setStageName("EdgeToRaw");
				  processValidationResult.setFileName(inputFile);
				  processValidationResult.setStatus("COMPLETED");
				  
				  executionContext.getTaskSatusDetailsMap().put(executionTaskType,processValidationResult);				  
				  executionContext.getTaskSatusMap().put(executionTaskType,ExecutionStatusType.COMPLETED); 
				//executionContext.getExecutionResult().getTaskSatusMap().put(Tasks.DBValidationTask.toString(),TaskStatus.Success.toString());
				//wTEUtils.jaxbObjectToXML(executionContext, "");
	         } catch (Exception e) {
	        	 WTEUtils.updateStatus(executionContext, this.executionTaskType, ExecutionStatusType.ERROR);
	        	 WTEUtils.jaxbObjectToXML(executionContext, "");
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
     	
		
	}

	@Override
	public void postProcess(ExecutionContext executionContext) {
		if(WTEUtils.getStatus(executionContext, executionTaskType)!=ExecutionStatusType.ERROR){
			WTEUtils.updateStatus(executionContext, this.executionTaskType, ExecutionStatusType.COMPLETED);
	     		
		}
		
	}
}
