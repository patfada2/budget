package mesh.budget.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import mesh.budget.model.BankStatementRow;
import mesh.budget.model.Categories;
import mesh.budget.model.Category;

public class CategoryUIController {
	
	private Categories categories;
	

	private static final Logger logger = LoggerFactory.getLogger(CategoryUIController.class);
	static String categoryFileName = "C:\\Users\\patri\\git\\budget\\categories.csv";
	
	private BankStatementRow selectedRow;
	
	private String selection;

	public String getSelection() {
		return selection;
	}

	@FXML
	private BorderPane borderPane;
	@FXML
	private ListView list1;

	@FXML
	private Button okButton;
	
	@FXML
	private Button addButton;

	@FXML
	public void initialize() {
		logger.info("intitializing catcontroler="+this.toString());
		
		
		categories  = new Categories();
		categories.loadFromFile(categoryFileName);
		
		ObservableList<String> names = FXCollections.observableArrayList();

		for (Category c : categories.getCategories()) {
			names.add(c.getName());
		}

		list1.setItems(names);
		

		list1.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				logger.info(newValue);
				selection = newValue;

			}
		});
	}

	@FXML
	public void onOKClick(ActionEvent event) {
		logger.info("category ok button clicked");
		logger.info("selected row="+selectedRow.getDescription());
		
		selectedRow.setCategory(selection);
		logger.info("selected row's category:" + selectedRow.getCategory());
		
		Stage stage = (Stage) borderPane.getScene().getWindow();
		stage.hide();
 
	}
	
	@FXML
	public void onAddClick(ActionEvent event) {
		logger.info("onAddClick");
		
	}


	public void setSelectedRow(BankStatementRow selectedRow) {
		this.selectedRow = selectedRow;
		logger.info(selectedRow.getDescription());
	}

	public CategoryUIController() {
		logger.info("constructing catcontroler="+this.toString());
		
	}
	
	
	public void show() {
	
		Stage stage = (Stage) borderPane.getScene().getWindow();
		stage.show();
		
	}
	
}
