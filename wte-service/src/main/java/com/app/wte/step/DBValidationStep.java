package com.app.wte.step;

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
import com.app.wte.util.WTEUtils;

@Service(value="dBValidationStep")
public class DBValidationStep implements TestExecutionStep {
	
	
	@Value("${upload-path}")
	public String uploadPath;
	
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
							  
							  processValidationResult.setFileName(result.getOrDefault("FILE_NAME","").toString());
							  processValidationResult.setEfid(result.getOrDefault("EFID","").toString());
							  processValidationResult.setClientName(result.getOrDefault("CLIENT_NAME","").toString());
							  processValidationResult.setSourcePath(result.getOrDefault("SOURCE_PATH","").toString());
							  processValidationResult.setStageName(result.getOrDefault("STAGE_NAME","").toString());
							  processValidationResult.setStatus(result.getOrDefault("STATUS","").toString());
							  processValidationResult.setTotalRecordsCount((int)(result.getOrDefault("TOTAL_RECORDS_COUNT",0)));
							  processValidationResult.setValidRecordsCount((int)(result.getOrDefault("VALID_RECORDS_COUNT",0)));
							  processValidationResult.setInvalidRecordsCount((int)(result.getOrDefault("INVALID_RECORDS_COUNT",0)));
							  processValidationResult.setFileArrivalTimestamp(result.get("EDGE_NODE_FILE_ARRIVAL_TIMESTAMP")!=null?
									  (Date)(result.get("EDGE_NODE_FILE_ARRIVAL_TIMESTAMP")):null);
							  processValidationResult.setRuleVersion((double)(result.getOrDefault("RULE_VERSION",0.0)));
							  processValidationResult.setIsCompressed(result.getOrDefault("IS_COMPRESSED","").toString());
							  processValidationResult.setProcessedTimestamp(result.get("PROCESSED_TIMESTAMP")!=null?
									  (Date)(result.get("PROCESSED_TIMESTAMP")):null);
							  processValidationResult.setProcessedUserId(result.getOrDefault("PROCESSED_USER_ID","").toString());
							  
							  executionTaskType = WTEUtils.getExecutionStep(executionContext,result.getOrDefault("STAGE_NAME","").toString());
							  
							  executionContext.getTaskSatusDetailsMap().put(executionTaskType,processValidationResult);				  
							  executionContext.getTaskSatusMap().put(executionTaskType,ExecutionStatusType.COMPLETED);
							  
							  WTEUtils.jaxbObjectToXML(executionContext,uploadPath,"");
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
	         } catch (Exception e) {
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
		 WTEUtils.jaxbObjectToXML(executionContext,uploadPath, "");
		
	}

	@Override
	public void postProcess(ExecutionContext executionContext) {
		if(WTEUtils.getStatus(executionContext, executionTaskType)!=ExecutionStatusType.ERROR){
			WTEUtils.updateStatus(executionContext, this.executionTaskType, ExecutionStatusType.COMPLETED);
			WTEUtils.jaxbObjectToXML(executionContext,uploadPath, "");	
		}
		
	}
}
