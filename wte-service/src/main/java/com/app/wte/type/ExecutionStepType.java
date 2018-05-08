package com.app.wte.type;

public enum ExecutionStepType {
	
	FileGeneration("FileGeneration"),
	FTPTransfer("FTPTransfer"),
	/*Verification("Verification"),*/
	ProcessValidationEdgeToRaw("EDGE_TO_RAW"),
	ProcessValidationRawToRA("Raw_To_RA"),
	ProcessValidationRAToRaw("RA_TO_RAW"),
	ProcessValidationRawToR("Raw_To_R"); 
	
	private String executionStep;
	
	public String getExecutionStep()
    {
        return this.executionStep;
    }
 
    private ExecutionStepType(String executionStep)
    {
        this.executionStep = executionStep;
    }

}
