package com.app.wte.ui;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;

import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringComponent;

/**
 * This class provides an interface for the logical operations between the CRUD
 * view, its parts like the product editor form and the data provider, including
 * fetching and saving products.
 *
 * Having this separate from the view makes it easier to test various parts of
 * the system separately, and to e.g. provide alternative views for the same
 * data.
 */
@SuppressWarnings("serial")
@SpringComponent
public class TestRunLogic implements Serializable {

	private TestRunView view;

	@Scope(scopeName = ConfigurableBeanFactory.SCOPE_SINGLETON)
	@SpringComponent
	public static class TestRunLogicFactory {

		@Autowired
		private ApplicationContext context;

		public TestRunLogic createLogic(TestRunView view) {
			TestRunLogic logic = context.getBean(TestRunLogic.class);
			logic.init(view);
			return logic;
		}
	}

	private TestRunLogic() {
	}

	public void init() {
		// Hide and disable if not admin
		view.setNewProductEnabled(true);

	}

	public void cancelProduct() {
		setFragmentParameter("");
		view.clearSelection();

	}

	/**
	 * Update the fragment without causing navigator to change view
	 */
	private void setFragmentParameter(String productId) {
		String fragmentParameter;
		if (productId == null || productId.isEmpty()) {
			fragmentParameter = "";
		} else {
			fragmentParameter = productId;
		}

		Page page = MainUI.get().getPage();
		page.setUriFragment("!" + TestRunView.VIEW_PATH + "/" + fragmentParameter, false);
	}

	public void enter(String productId) {
		if (productId != null && !productId.isEmpty()) {
			if (productId.equals("new")) {
				newTestCase();
			} else {
				// Ensure this is selected even if coming directly here from
				// login
				try {
					int pid = Integer.parseInt(productId);
					Product product = findProduct(pid);
					view.selectRow(product);
				} catch (NumberFormatException e) {
				}
			}
		}
	}

	private Product findProduct(int productId) {
		return null;
	}

	public void saveProduct(Product product) {
		view.showSaveNotification(product.getProductName() + " (" + product.getId() + ") updated");
		view.clearSelection();
		view.editProduct(null);
		view.updateProduct(product);
		setFragmentParameter("");
	}

	public void deleteProduct(Product product) {

		view.showSaveNotification(product.getProductName() + " (" + product.getId() + ") removed");

		view.clearSelection();
		view.editProduct(null);
		view.removeProduct(product);
		setFragmentParameter("");
	}

	public void editProduct(TestRunUI product) {
		if (product == null) {
			setFragmentParameter("");
		} else {
			setFragmentParameter(product.getId() + "");
		}
		view.editProduct(product);
	}

	public void newTestCase() {
		view.clearSelection();
		setFragmentParameter("new");
		view.editProduct(new TestRunUI());
	}

	public void rowSelected(TestRunUI product) {
		view.editProduct(product);

	}

	private void init(TestRunView view) {
		this.view = view;
	}
}
