package com.app.wte.ui;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.app.wte.ui.HomeView.HomeFormFactory;
import com.app.wte.ui.TestCaseLogic.TestCaseLogicFactory;
import com.vaadin.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * A view for performing create-read-update-delete operations on products.
 *
 * See also {@link TestCaseLogic} for fetching the data, the actual CRUD
 * operations and controlling the view based on events from outside.
 */
@SuppressWarnings("serial")
@SpringView(name = TestCaseView.VIEW_PATH)
public class TestCaseView extends CssLayout implements View {

	public static final String VIEW_NAME = "TEST CASE";
	
	public static final String VIEW_PATH = "testCase";

	@Autowired
	private HomeFormFactory formFactory;

	private ProductGrid grid;

	private HomeView form;

	private TestCaseLogic viewLogic;

	private Button newProduct;

	@Autowired
	private TestCaseLogicFactory logicFactory;
	private TextField filter;
	private ConfigurableFilterDataProvider<TestCaseUI, Void, String> filterDataProvider;

	public HorizontalLayout createTopBar() {
		filter = new TextField();
		filter.setStyleName("filter-textfield");
		filter.setPlaceholder("Filter");
		// Trigger a refresh of data when the filter is updated
		filter.addValueChangeListener(event -> filterDataProvider.setFilter(event.getValue()));

		newProduct = new Button("New Test Case");
		newProduct.addStyleName(ValoTheme.BUTTON_PRIMARY);
		newProduct.setIcon(VaadinIcons.PLUS_CIRCLE);
		newProduct.addClickListener(event -> viewLogic.newTestCase());
		
		HorizontalLayout topLayout = new HorizontalLayout();
		topLayout.setWidth("100%");
		topLayout.addComponent(filter);
		topLayout.addComponent(newProduct);
		topLayout.setComponentAlignment(filter, Alignment.MIDDLE_LEFT);
		topLayout.setExpandRatio(filter, 1);
		topLayout.setStyleName("top-bar");
		return topLayout;
	}

	@Override
	public void enter(ViewChangeEvent event) {
		viewLogic.enter(event.getParameters());
	}

	public void showError(String msg) {
		Notification.show(msg, Type.ERROR_MESSAGE);
	}

	public void showSaveNotification(String msg) {
		Notification.show(msg, Type.TRAY_NOTIFICATION);
	}

	public void setNewProductEnabled(boolean enabled) {
		newProduct.setEnabled(enabled);
	}

	public void clearSelection() {
		grid.getSelectionModel().deselectAll();
	}

	public void selectRow(Product row) {
		grid.getSelectionModel().select(row);
	}

	public Product getSelectedRow() {
		return grid.getSelectedRow();
	}

	public void editProduct(TestCaseUI product) {
		if (product != null) {
			form.addStyleName("visible");
			form.setEnabled(true);
		} else {
			form.removeStyleName("visible");
			// Issue #286
			// form.setEnabled(false);
		}
		form.editProduct(product);
	}

	public void updateProduct(Product product) {
		// dataProvider.save(product);
		// TODO: Grid used to scroll to the updated item
	}

	public void removeProduct(Product product) {
		// dataProvider.delete(product);
	}

	@PostConstruct
	private void init() {
		System.out.println("TestCaseView entry");

		viewLogic = logicFactory.createLogic(this);

		setSizeFull();
		addStyleName("crud-view");
		HorizontalLayout topLayout = createTopBar();

		grid = new ProductGrid();
		grid.asSingleSelect().addValueChangeListener(event -> {
//			viewLogic.rowSelected(grid.getSelectedRow());
			});

		// filterDataProvider = dataProvider.withConfigurableFilter();
		// grid.setDataProvider(filterDataProvider);

		VerticalLayout barAndGridLayout = new VerticalLayout();
		barAndGridLayout.addComponent(topLayout);
		barAndGridLayout.addComponent(grid);
		barAndGridLayout.setSizeFull();
		barAndGridLayout.setMargin(false);
		barAndGridLayout.setSpacing(false);
		barAndGridLayout.setExpandRatio(grid, 1);
		barAndGridLayout.setStyleName("crud-main-layout");

		addComponent(barAndGridLayout);

		form = formFactory.createForm(viewLogic);
		// form.setCategories(dataService.getAllCategories());
		addComponent(form);

		viewLogic.init();
	}

}
