package com.app.wte.ui;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;

@SpringUI
public class MainUI extends UI{

	@Autowired
	private CustomComponents customComponents;
	
	@Override
	protected void init(VaadinRequest request) {
		// TODO Auto-generated method stub
		setContent(customComponents);
	}

}
