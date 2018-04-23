package com.app.wte.step;

import com.app.wte.model.ExecutionContext;

public interface TestExecutionStep {
	public void preprocess(ExecutionContext executionContext);
	public void execute(ExecutionContext executionContext);
	public void postProcess(ExecutionContext executionContext);
	
}
