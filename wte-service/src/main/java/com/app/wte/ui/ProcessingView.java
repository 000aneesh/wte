package com.app.wte.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.app.wte.model.ExecutionContext;
import com.app.wte.model.ProcessValidationResult;
import com.app.wte.testengine.TestEngine;
import com.app.wte.type.ExecutionStatusType;
import com.app.wte.type.ExecutionStepType;
import com.app.wte.util.WTEUtils;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")

//@SpringComponent
//@Scope("prototype")
@SpringView(name = ProcessingView.VIEW_PATH)
public class ProcessingView extends Processing implements View {

	@Value("${upload-path}")
	private String uploadPath;
	
	public static final String VIEW_PATH = "processing";
	Map<String, String> processStatus;

	UI ui = UI.getCurrent();

	public ProcessingView() {
	}

	// private String testCase;
	// private String executionStep;

	@Autowired
	TestEngine testEngine;

	@PostConstruct
	public void init() throws IOException {
		// layout = new HorizontalLayout();
		// addComponent(createGrid());

		System.out.println("init");
	}

	@Override
	public void enter(ViewChangeEvent event) {
		System.out.println("enter");
		// Notification.show("Welcome to ProcessingView");

		if (event.getParameters() != null) {
			// split at "/", add each part as a label
			String[] params = event.getParameters().split("/");
			String testCase = params[0];
			String template = params[1];
			System.out.println("testCase : " + testCase);

			UI.getCurrent().setPollInterval(500);
			
			// String[] executionSteps = WTEUtils.getEnumArray(ExecutionStepType.class);
			// String executionStep = executionSteps[0];
			setTimeout(() -> {
				try {
					updateStatus(testCase, template);
				} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException
						| SecurityException e) {
					e.printStackTrace();
				}
			}, 1000);
			
			
			
			pageInit();
			/*
			 * try {
			 * 
			 * updateStatus(testCase); } catch (IllegalArgumentException |
			 * IllegalAccessException | NoSuchFieldException | SecurityException e) { //
			 * TODO Auto-generated catch block e.printStackTrace(); }
			 */
		}
	}

	public void updateStatus(String testCase, String template)
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {

//		downloadButton.addClickListener(evnt -> download(testCase, template));
		
		processStatus = new HashMap<String, String>();

		// ui.access(() -> progressBar0.setValue(0.1f));

		for (ExecutionStepType executionStepType : ExecutionStepType.values()) {
			String executionStep = executionStepType.getExecutionStep();
			// ProcessingView.class.getField("progressBar" + index).setFloat(this, 0.0f);
			ExecutionStatusType resultStatus = getResult(testCase, executionStep);
			System.out.println(
					"1 resultStatus: " + resultStatus + " testCase: " + testCase + " executionStep: " + executionStep);
			while (resultStatus == null || ExecutionStatusType.IN_PROGRESS.equals(resultStatus)) {
				if (ExecutionStepType.FileGeneration.getExecutionStep().equals(executionStep)) {
					processInProgress(progressBar0, success0, fail0);
				} else if (ExecutionStepType.FTPTransfer.getExecutionStep().equals(executionStep)) {
					processInProgress(progressBar1, success1, fail1);
				} else if (ExecutionStepType.ProcessValidationEdgeToRaw.getExecutionStep().equals(executionStep)) {
					processInProgress(progressBar2, success2, fail2);
				} else if (ExecutionStepType.ProcessValidationRawToRA.getExecutionStep().equals(executionStep)) {
					processInProgress(progressBar3, success3, fail3);
				} else if (ExecutionStepType.ProcessValidationRAToRaw.getExecutionStep().equals(executionStep)) {
					processInProgress(progressBar4, success4, fail4);
				} else if (ExecutionStepType.ProcessValidationRawToR.getExecutionStep().equals(executionStep)) {
					processInProgress(progressBar5, success5, fail5);
				}

				/*
				 * float current = progressBar0.getValue(); if (current < 1.0f) { //
				 * ProcessingView.class.getField("progressBar" + index).setFloat(this, current +
				 * // 0.10f); progressBar0.setValue(current + 0.10f); }
				 */
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
				// Processing.class.getField("progressBar" + index).setFloat(this, 1.0f);
				if (ExecutionStepType.FileGeneration.getExecutionStep().equals(executionStep)) {
					processCompleted(progressBar0, success0, fail0);
					download(testCase, template);
					downloadButton.setVisible(true);
				} else if (ExecutionStepType.FTPTransfer.getExecutionStep().equals(executionStep)) {
					processCompleted(progressBar1, success1, fail1);
				} else if (ExecutionStepType.ProcessValidationEdgeToRaw.getExecutionStep().equals(executionStep)) {
					processCompleted(progressBar2, success2, fail2);
				} else if (ExecutionStepType.ProcessValidationRawToRA.getExecutionStep().equals(executionStep)) {
					processCompleted(progressBar3, success3, fail3);
				} else if (ExecutionStepType.ProcessValidationRAToRaw.getExecutionStep().equals(executionStep)) {
					processCompleted(progressBar4, success4, fail4);
				} else if (ExecutionStepType.ProcessValidationRawToR.getExecutionStep().equals(executionStep)) {
					processCompleted(progressBar5, success5, fail5);
					System.out.println("final step : " + executionStep);
					ui.setPollInterval(-1);
				}

				ProcessValidationResult processValidationResult = getResultDetails(testCase, executionStep);
				System.out.println("3 processValidationResult: " + resultStatus + " testCase: " + testCase
						+ " executionStep: " + executionStep + " processValidationResult : " + processValidationResult);
				processStatus.put(executionStep, ExecutionStatusType.COMPLETED.toString());
				// refreshGrid();

			} else if (ExecutionStatusType.ERROR.equals(resultStatus)) {
				if (ExecutionStepType.FileGeneration.getExecutionStep().equals(executionStep)) {
					processError(progressBar0, success0, fail0);
				} else if (ExecutionStepType.FTPTransfer.getExecutionStep().equals(executionStep)) {
					processError(progressBar1, success1, fail1);
				} else if (ExecutionStepType.ProcessValidationEdgeToRaw.getExecutionStep().equals(executionStep)) {
					processError(progressBar2, success2, fail2);
				} else if (ExecutionStepType.ProcessValidationRawToRA.getExecutionStep().equals(executionStep)) {
					processError(progressBar3, success3, fail3);
				} else if (ExecutionStepType.ProcessValidationRAToRaw.getExecutionStep().equals(executionStep)) {
					processError(progressBar4, success4, fail4);
				} else if (ExecutionStepType.ProcessValidationRawToR.getExecutionStep().equals(executionStep)) {
					processError(progressBar5, success5, fail5);
				}
				System.out.println("4 resultStatus: " + resultStatus + " testCase: " + testCase + " executionStep: "
						+ executionStep);
				ui.setPollInterval(-1);
				break;
			}
		}

	}

	private void processInProgress(ProgressBar progressBar, Label success, Label fail) {
		if (progressBar.getValue() < 0.9f) {
			ui.access(() -> {
				progressBar.setValue(progressBar.getValue() + 0.10f);
				progressBar.setCaption("Processing...");
				success.setVisible(false);
				fail.setVisible(false);
			});
		}
	}
	
	private void processCompleted(ProgressBar progressBar, Label success, Label fail) {
		ui.access(() -> {
			progressBar.setVisible(false);
			success.setVisible(true);
			fail.setVisible(false);
		});
	}
	
	private void processError(ProgressBar progressBar, Label success, Label fail) {
		ui.access(() -> {
			progressBar.setVisible(false);
			success.setVisible(false);
			fail.setVisible(true);
		});
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

	public static void setTimeout(Runnable runnable, int delay) {
		new Thread(() -> {
			try {
				Thread.sleep(delay);
				runnable.run();
			} catch (Exception e) {
				System.err.println(e);
			}
		}).start();
	}
	
	private void pageInit() {
		//progressBar0.setWidth(200, Sizeable.Unit.PIXELS);
		downloadButton.setVisible(false);
		fail0.setVisible(false);
		fail1.setVisible(false);
		fail2.setVisible(false);
		fail3.setVisible(false);
		fail4.setVisible(false);
		fail5.setVisible(false);
		success0.setVisible(false);
		success1.setVisible(false);
		success2.setVisible(false);
		success3.setVisible(false);
		success4.setVisible(false);
		success5.setVisible(false);
	}
	
	private void download(String testCase, String template) {
		StreamResource myResource = createResource(testCase, template);
        FileDownloader fileDownloader = new FileDownloader(myResource);
        if(fileDownloader.getExtensions()!=null && fileDownloader.getExtensions().size()>0){
             fileDownloader.remove();
        }
        fileDownloader.extend(downloadButton);

	}
	
	public StreamResource createResource(String testCase, String template) {

        return new StreamResource(new StreamSource() {
               @Override
               public InputStream getStream() {
                     try {
                    	String filePath = uploadPath + File.separator + testCase + File.separator + "TestData" + File.separator +  template + "_" +testCase + ".txt";
                    	File file = new File(filePath);
                            return new FileInputStream(file);
                     } catch (FileNotFoundException e) {
                            e.printStackTrace();
                     }
                     return null;

               }

        }, testCase + ".txt");
 }


}
