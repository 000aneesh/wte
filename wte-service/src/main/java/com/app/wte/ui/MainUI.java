package com.app.wte.ui;

import org.springframework.beans.factory.annotation.Autowired;

import com.app.wte.constants.WTEConstants;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;

@SpringUI
public class MainUI extends UI{

	@Autowired
	private HomeView homeView;
	
	@Autowired
	private ProcessingView processingView;
	
	
	Navigator navigator;
	
	@Override
	protected void init(VaadinRequest request) {
		//getPage().setTitle("Home Page");
		navigator = new Navigator(this, this);

        // Create and register the views
     
        navigator.addView(WTEConstants.HOMEVIEW, homeView);
        navigator.addView(WTEConstants.PROCESSINGVIEW, processingView);
	//	setContent(homeView);
	}

}
