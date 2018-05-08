package com.app.wte.ui;

import org.springframework.beans.factory.annotation.Autowired;

import com.app.wte.ui.authentication.AccessControl;
import com.app.wte.ui.authentication.LoginScreen;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Viewport;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
@Viewport("user-scalable=no,initial-scale=1.0")
@Theme("mytheme")
@SpringUI
@Push
public class MainUI extends UI{

	/*@Autowired
	private HomeView homeView;
	
	@Autowired
	private ProcessingView processingView;*/
	
	@Autowired
    private SpringViewProvider viewProvider;
	
	@Autowired
    private AccessControl accessControl;
	
	private Menu menu;
	
	
	//Navigator navigator;
	
	CssLayout viewContainer = new CssLayout();
	
	@Override
	protected void init(VaadinRequest vaadinRequest) {
		
		Responsive.makeResponsive(this);
        setLocale(vaadinRequest.getLocale());
        getPage().setTitle("WTE");
        if (!accessControl.isUserSignedIn()) {
            setContent(new LoginScreen(accessControl, this::showMainView));
        } else {
            showMainView();
        }
        
		//getPage().setTitle("Home Page");
		
		
		/*navigator = new Navigator(this, this);

        // Create and register the views
     
        navigator.addView(WTEConstants.HOMEVIEW, homeView);
        navigator.addView(WTEConstants.PROCESSINGVIEW, processingView);*/
		
		
	//	setContent(homeView);
	}
	
    protected void showMainView() {
        addStyleName(ValoTheme.UI_WITH_MENU);

        HorizontalLayout layout = new HorizontalLayout();
        setContent(layout);

        layout.setStyleName("main-screen");

       // CssLayout viewContainer = new CssLayout();
        viewContainer.addStyleName("valo-content");
        viewContainer.setSizeFull();

        Navigator navigator = new Navigator(this, viewContainer);
        navigator.addProvider(viewProvider);
        navigator.setErrorView(ErrorView.class);

        menu = new Menu(navigator);
        // View are registered automatically by Vaadin Spring support
        menu.addViewButton(HomeViewBkup.VIEW_PATH, HomeViewBkup.VIEW_NAME,
                VaadinIcons.EDIT);
        menu.addViewButton(HomeViewBkup.VIEW_PATH, HomeViewBkup.VIEW_NAME,
                VaadinIcons.INFO_CIRCLE);
        menu.addViewButton(TestCaseView.VIEW_PATH, TestCaseView.VIEW_NAME, VaadinIcons.EXTERNAL_LINK);
        
        menu.addViewButton(SampleCrudView.VIEW_NAME, SampleCrudView.VIEW_NAME, VaadinIcons.EXTERNAL_LINK);

        navigator.addViewChangeListener(new ViewChangeHandler());

        layout.addComponent(menu);
        layout.addComponent(viewContainer);
        layout.setExpandRatio(viewContainer, 1);
        layout.setSizeFull();
        layout.setSpacing(false);

     //   navigator.navigateTo(HomeView.VIEW_PATH);
        navigator.navigateTo(TestCaseView.VIEW_PATH);
    }
    
    public static MainUI get() {
        return (MainUI) UI.getCurrent();
    }

    public AccessControl getAccessControl() {
        return accessControl;
    }
    
	protected void navigateTo(String viewName) {
		Navigator navigator = new Navigator(this, viewContainer);
		navigator.addProvider(viewProvider);
		
		navigator.setErrorView(ErrorView.class);

		System.out.println("navigateTo entry");
		menu.setActiveView(viewName);
		navigator.navigateTo(viewName);
		
	}
    
    private class ViewChangeHandler implements ViewChangeListener {

        @Override
        public boolean beforeViewChange(ViewChangeEvent event) {
            return true;
        }

        @Override
        public void afterViewChange(ViewChangeEvent event) {
            menu.setActiveView(event.getViewName());
        }

    };

}
