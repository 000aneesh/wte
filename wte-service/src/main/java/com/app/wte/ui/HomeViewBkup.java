package com.app.wte.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.app.wte.constants.WTEConstants;
import com.app.wte.testengine.TestEngine;
import com.app.wte.util.ConfigurationComponent;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.FinishedEvent;
import com.vaadin.ui.Upload.ProgressListener;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.StartedEvent;
import com.vaadin.ui.Upload.StartedListener;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;

/*
@SpringComponent
@Scope("prototype")*/
@SuppressWarnings("serial")
@SpringView(name = HomeViewBkup.VIEW_PATH)
public class HomeViewBkup extends Home implements View {
	
	public static final String VIEW_NAME = "HOME";
	public static final String VIEW_PATH = "home";

	@Value("${upload-path}")
	private String uploadPath;

	@Autowired
	ConfigurationComponent confComponent;

	@Autowired
	TestEngine testEngine;

	public HomeViewBkup() {

	}

	private String fileName;

	@PostConstruct
	public void init() throws IOException {
		Set<String> templatesList = confComponent.getTemplates();
		// templatesList.add(0, "Select One");
		templateName.setPlaceholder("Select One");
		templateName.setItems(templatesList);
		templateName.setEmptySelectionAllowed(false);
		// templateName.
		upload();

	}

	private void upload() {

		upload.setReceiver(receiver);
		upload.setImmediateMode(false);
		upload.setButtonCaption("Submit");
		upload.addSucceededListener(succeededListener);
		final long UPLOAD_LIMIT = 1000000l;
		upload.addStartedListener(new StartedListener() {
			@Override
			public void uploadStarted(StartedEvent event) {
				System.out.println("uploadStarted " + event.getContentLength() + " " + event.getFilename());
				if (event.getContentLength() == 0 || testCase.getValue() == null
						|| testCase.getValue().trim().equals("") || templateName.getValue() == null
						|| templateName.getValue().trim().equals("")) {
					Notification.show("All fields are mandatory", Notification.Type.WARNING_MESSAGE);
					upload.interruptUpload();
				}
				if (event.getContentLength() > UPLOAD_LIMIT) {
					Notification.show("Too big file", Notification.Type.WARNING_MESSAGE);
					upload.interruptUpload();
				}

			}
		});

		// Check the size also during progress
		upload.addProgressListener(new ProgressListener() {
			@Override
			public void updateProgress(long readBytes, long contentLength) {
				if (readBytes > UPLOAD_LIMIT) {
					Notification.show("Too big file", Notification.Type.WARNING_MESSAGE);
					upload.interruptUpload();
				}
			}
		});

		upload.addFinishedListener(new Upload.FinishedListener() {

			@Override
			public void uploadFinished(FinishedEvent event) {

				// downloadBtn.setVisible(true);
				// StreamResource myResource = createResource();
				// FileDownloader fileDownloader = new FileDownloader(myResource);
				// fileDownloader.extend(downloadBtn);
			}

			// private StreamResource createResource() {
			//
			// return new StreamResource(new StreamSource() {
			// @Override
			// public InputStream getStream() {
			// try {
			// return new FileInputStream(outputFile);
			// } catch (FileNotFoundException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// return null;
			//
			// }
			//
			// }, "CPWorkBookOut.xlsx");
			//
			// }
		});

		upload.addFailedListener(new Upload.FailedListener() {

			@Override
			public void uploadFailed(FailedEvent event) {
				// TODO Auto-generated method stub

				System.out.println("error ............");
				upload.interruptUpload();
			}
		});

	}

	Receiver receiver = new Receiver() {

		private static final long serialVersionUID = 1L;

		@Override
		public OutputStream receiveUpload(String filename, String mimeType) {

			if (filename != null && !filename.trim().equals("")) {
				fileName = filename;
				System.out.println("receiveUpload - filename : " + filename);
				// downloadBtn.setVisible(false);
				// Create upload stream
				FileOutputStream fos = null; // Stream to write to
				try {
					// String uniqueTimeStamp = WTEUtils.getUniqueTimeStamp();
					Path fileLocation = Paths
							.get(uploadPath + File.separator + testCase.getValue() + File.separator + "TestData");

					if (!Files.exists(fileLocation)) {
						try {
							Files.createDirectories(fileLocation);
						} catch (IOException e) {
							throw new RuntimeException("Could not initialize storage!");
						}
						// Open the file for writing.
						File outputFile = new File(fileLocation + File.separator + filename);
						fos = new FileOutputStream(outputFile);
					}else {
						Notification.show("Test case already exists", Notification.Type.WARNING_MESSAGE);
					}

				} catch (final java.io.FileNotFoundException e) {
					// new Notification("Could not open file<br/>", e.getMessage(),
					// Notification.Type.ERROR_MESSAGE)
					// .show(Page.getCurrent());
					Notification.show("Could not open file<br/>" + e.getMessage(), Notification.Type.WARNING_MESSAGE);
					return null;
				}
				return fos; // Return the output stream to write to

			}
			return null;

		}
	};
	SucceededListener succeededListener = new SucceededListener() {

		private static final long serialVersionUID = 1L;

		@Override
		public void uploadSucceeded(SucceededEvent event) {

			// Notification.show("File uploaded successfully",
			// Notification.Type.TRAY_NOTIFICATION);
			testEngine.createTestSuite(testCase.getValue(), fileName, templateName.getValue(), testCase.getValue());

			getUI().getNavigator().navigateTo(WTEConstants.PROCESSINGVIEW + "/" + testCase.getValue());
			// System.out.println("uploadSucceeded");

			// outputFile = receiveUpload(inputFile);

		}

	};

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}

}
