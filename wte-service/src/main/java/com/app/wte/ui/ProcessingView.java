package com.app.wte.ui;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.wte.model.ExecutionContext;
import com.app.wte.model.ProcessValidationResult;
import com.app.wte.testengine.TestEngine;
import com.app.wte.type.ExecutionStatusType;
import com.app.wte.type.ExecutionStepType;
import com.app.wte.util.WTEUtils;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;

@SuppressWarnings("serial")
/*@SpringComponent
@Scope("prototype")*/
@SpringView(name = ProcessingView.VIEW_PATH)
public class ProcessingView extends Processing implements View {

	public static final String VIEW_PATH = "processing";
	
	public ProcessingView() {
	}

	// private String testCase;
	// private String executionStep;

	@Autowired
	TestEngine testEngine;

	@PostConstruct
	public void init() throws IOException {
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// Notification.show("Welcome to ProcessingView");

		if (event.getParameters() != null) {
			// split at "/", add each part as a label
			String[] params = event.getParameters().split("/");
			String testCase = params[0];
			System.out.println("testCase : " + testCase);

			// String[] executionSteps = WTEUtils.getEnumArray(ExecutionStepType.class);
			// String executionStep = executionSteps[0];
			try {
				updateStatus(testCase);
			} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void updateStatus(String testCase)
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {

	//	String[] executionSteps = WTEUtils.getEnumArray(ExecutionStepType.class);
		// progressBar0.setValue(0.0f);
		int index = 0;
		for (ExecutionStepType executionStepType : ExecutionStepType.values()) {
			String executionStep = executionStepType.getExecutionStep();
			// ProcessingView.class.getField("progressBar" + index).setFloat(this, 0.0f);
			progressBar0.setValue(0.0f);
			ExecutionStatusType resultStatus = getResult(testCase, executionStep);
			System.out.println(
					"1 resultStatus: " + resultStatus + " testCase: " + testCase + " executionStep: " + executionStep);
			while (ExecutionStatusType.IN_PROGRESS.equals(resultStatus)) {
//				Float current = ProcessingView.class.getField("progressBar" + index).getFloat(this);
				float current = progressBar0.getValue();
				if (current < 1.0f) {
					// ProcessingView.class.getField("progressBar" + index).setFloat(this, current +
					// 0.10f);
					progressBar0.setValue(current + 0.10f);
				}
				try {
					Thread.sleep(1000);
					resultStatus = getResult(testCase, executionStep);
					System.out.println("2 resultStatus: " + resultStatus + " testCase: " + testCase + " executionStep: "
							+ executionStep);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}

			if (ExecutionStatusType.COMPLETED.equals(resultStatus)) {
			//	Processing.class.getField("progressBar" + index).setFloat(this, 1.0f);
				progressBar0.setValue(1.0f);
				ProcessValidationResult processValidationResult = getResultDetails(testCase, executionStep);
				System.out.println("3 processValidationResult: " + resultStatus + " testCase: " + testCase
						+ " executionStep: " + executionStep + " processValidationResult : " + processValidationResult);

			} else if (ExecutionStatusType.ERROR.equals(resultStatus)) {
				System.out.println("4 resultStatus: " + resultStatus + " testCase: " + testCase + " executionStep: "
						+ executionStep);
				break;
			}
			index++;
		}

	}

	public ExecutionStatusType getResult(String testCase, String executionStep) {

		ExecutionStatusType executionStatusType = null;

		ExecutionContext executionContext = testEngine.getTestResult(testCase);
		if (executionContext != null) {
			executionStatusType = WTEUtils.getExecutionSatus(executionContext, executionStep);
		}
		return executionStatusType;
	}

	public ProcessValidationResult getResultDetails(String testCase, String executionStep) {

		ProcessValidationResult processValidationResult = null;

		ExecutionContext executionContext = testEngine.getTestResult(testCase);
		if (executionContext != null && executionContext.getTaskSatusDetailsMap() != null) {
			ExecutionStepType executionStepType = WTEUtils.getExecutionStep(executionStep);
			processValidationResult = executionContext.getTaskSatusDetailsMap().get(executionStepType);
		}
		return processValidationResult;
	}

}
