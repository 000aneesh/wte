package com.app.wte.service;

import com.app.wte.model.TestRunRequest;

public interface Task {
	public void execute(TestRunRequest testRunRequest);
}
