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
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;

import com.app.wte.constants.WTEConstants;
import com.app.wte.testengine.TestEngine;
import com.app.wte.util.ConfigurationComponent;
import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.Binder;
import com.vaadin.data.StatusChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringComponent;
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

/**
 * A form for editing a single product.
 *
 * Using responsive layouts, the form can be displayed either sliding out on the
 * side of the view or filling the whole screen - see the theme for the related
 * CSS rules.
 */
@SuppressWarnings("serial")
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class HomeView extends Home {

    private TestRunLogic viewLogic;
    private final Binder<TestRunUI> binder = new BeanValidationBinder<>(TestRunUI.class);

    @Value("${upload-path}")
	private String uploadPath;

	@Autowired
	ConfigurationComponent confComponent;

	@Autowired
	TestEngine testEngine;
	
	private String fileName;

//    @Autowired
//    private SpringViewProvider viewProvider;

    @SpringComponent
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    public static class HomeFormFactory {

        @Autowired
        private ApplicationContext context;

        public HomeView createForm(TestRunLogic logic) {
            HomeView form = context.getBean(HomeView.class);
            form.init(logic);
            return form;
        }
    }
    
    private TestRunUI currentProduct;

    private HomeView() {
    }

    public void editProduct(TestRunUI product) {
        currentProduct = product;
        setUpData();

        delete.setEnabled(product != null && product.getId() != -1);

        // Scroll to the top
        // As this is not a Panel, using JavaScript
        String scrollScript = "window.document.getElementById('" + getId()
                + "').scrollTop = 0;";
        Page.getCurrent().getJavaScript().execute(scrollScript);
    }

    @PostConstruct
    private void init() throws IOException {
        addStyleName("product-form");

        Set<String> templatesList = confComponent.getTemplates();
		// templatesList.add(0, "Select One");
		templateName.setPlaceholder("Select One");
		templateName.setItems(templatesList);
		templateName.setEmptySelectionAllowed(false);

        binder.forField(testCase).bind("testCase");

        binder.forField(templateName).bind("templateName");

        save.addClickListener(event -> onSave());

        cancel.addClickListener(event -> viewLogic.cancelProduct());
        delete.addClickListener(event -> onDelete());
        discard.addClickListener(event -> setUpData());
        	
        binder.addStatusChangeListener(this::updateButtons);
        
        upload();
        
    }

    private void onSave() {
    	MainUI ui= MainUI.get();
    	ui.navigateTo(ProcessingView.VIEW_PATH);
        
        if (binder.writeBeanIfValid(currentProduct)) {
            //viewLogic.saveProduct(currentProduct);
        }
    }

    private void onDelete() {
        if (currentProduct != null) {
            //viewLogic.deleteProduct(currentProduct);
        }
    }

    private void init(TestRunLogic logic) {
        viewLogic = logic;
    }

    private void updateButtons(StatusChangeEvent event) {
        boolean changes = event.getBinder().hasChanges();
        boolean validationErrors = event.hasValidationErrors();

        save.setEnabled(!validationErrors && changes);
        discard.setEnabled(changes);
    }

    private void setUpData() {
        if (currentProduct != null) {
            binder.readBean(currentProduct);
        } else {
            binder.removeBean();
        }
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

			}

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

			testEngine.createTestSuite(testCase.getValue(), fileName, templateName.getValue(), testCase.getValue());

			getUI().getNavigator().navigateTo(WTEConstants.PROCESSINGVIEW + "/" + testCase.getValue() + "/" + templateName.getValue());

		}

	};

}
