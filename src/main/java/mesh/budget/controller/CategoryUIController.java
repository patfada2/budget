package mesh.budget.controller;

import java.util.List;

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
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import mesh.budget.model.BankStatementRow;
import mesh.budget.model.Categories;
import mesh.budget.model.Category;

public class CategoryUIController {
	
	private Categories categories;
	private ObservableList<String> names; 
	private ObservableList<String> matches; 
	

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
	private ListView listDetail;
	
	@FXML
	private Button okButton;
	
	@FXML
	private Button addButton;
	
	@FXML
	private TextField newCatText;

	@FXML
	public void initialize() {
		logger.info("intitializing catcontroler="+this.toString());
		
		
		categories  = new Categories();
		categories.loadFromFile(categoryFileName);
		names = FXCollections.observableArrayList();
		matches = FXCollections.observableArrayList();	
		
		list1.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				logger.info(newValue);
				selection = newValue;
				Category cat = categories.getCategoryByName(selection);
				loadMatches(cat);

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
		Category cat = new Category();
		cat.setName(newCatText.getText());
		categories.add(cat);
		
	}
	
	private void loadMatches(Category cat) {
		matches.clear();
		
		List<String> theMatches = cat.getMatches();
		for (int i=0; i < theMatches.size(); i++) {
			matches.add(theMatches.get(i));
		}
		listDetail.setItems(matches);
				
	}

	private void loadCategories() {
		names.clear();

		for (Category c : categories.getCategories()) {
			names.add(c.getName());
		}

		list1.setItems(names);
		
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
		loadCategories();
		newCatText.clear();
		stage.show();
		
	}
	
}
