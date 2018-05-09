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

@Service(value="dBValidationStep")
public class DBValidationStep implements TestExecutionStep {
	
	
	@Value("${upload-path}")
	public String uploadPath;
	
	@Autowired
	DBValidationDao dBValidationDao;
	
	ExecutionStepType executionStepType=ExecutionStepType.ProcessValidationEdgeToRaw;
	
	private int size = 0;
	
	@Override
	public void execute(ExecutionContext executionContext) {
		DBValidationResponse dbValidationResponse=null;
		
		String inputFile=executionContext.getConfigDataMap().get("templatePattern")+executionContext.getResultFolderName()+".txt";
			try {
				/*while(size<=4){
					 dbValidationResponse=dBValidationDao.getStageOfSourceFile(inputFile);
					  if(dbValidationResponse != null && dbValidationResponse.getResultList()!=null) {
						  List<Map<String, Object>> resultList=dbValidationResponse.getResultList();
						  for(Map<String, Object> result:resultList){
							  executionStepType = WTEUtils.processValidationResult(result,executionContext,uploadPath);
						  }
						  size=  resultList.size();
					  }
	                 try {
							Thread.sleep(30000);
	                 } catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
	                 }
	                        
	      	   }*/
				WTEUtils.mockProcessValidationResult(executionContext,inputFile,uploadPath);
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
		 WTEUtils.updateStatus(executionContext, ExecutionStepType.ProcessValidationEdgeToRaw, ExecutionStatusType.IN_PROGRESS);
		 WTEUtils.updateStatus(executionContext, ExecutionStepType.ProcessValidationRawToRA, ExecutionStatusType.IN_PROGRESS);
		 WTEUtils.updateStatus(executionContext, ExecutionStepType.ProcessValidationRAToRaw, ExecutionStatusType.IN_PROGRESS);
		 WTEUtils.updateStatus(executionContext, ExecutionStepType.ProcessValidationRawToR, ExecutionStatusType.IN_PROGRESS);
		 //WTEUtils.jaxbObjectToXML(executionContext,uploadPath, "");
		
	}

	@Override
	public void postProcess(ExecutionContext executionContext) {
		/*if(WTEUtils.getStatus(executionContext, executionStepType)!=ExecutionStatusType.ERROR){
			WTEUtils.updateStatus(executionContext, this.executionStepType, ExecutionStatusType.COMPLETED);
			WTEUtils.jaxbObjectToXML(executionContext,uploadPath, "");	
		}*/
		
	}
}
