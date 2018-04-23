package com.app.wte.type;

public enum ExecutionStepType {
	
	FileGeneration("FileGeneration"),
	FTPTransfer("FTPTransfer"),
	Verification("Verification"),
	ProcessValidationEdgeToRaw("ProcessValidationEdgeToRaw"),
	ProcessValidationRawToEdge("ProcessValidationRawToEdge");
	
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
