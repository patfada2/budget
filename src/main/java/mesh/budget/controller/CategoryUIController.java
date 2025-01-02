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
import mesh.budget.model.AppState;
import mesh.budget.model.BankStatementRow;
import mesh.budget.model.Categories;
import mesh.budget.model.Category;

public class CategoryUIController {
	
	private Categories categories;
	public void setCategories(Categories categories) {
		this.categories = categories;
	}

	private ObservableList<String> names; 
	private ObservableList<String> matches; 
	private AppState appStateModel;
	

	public AppState getAppStateModel() {
		return appStateModel;
	}

	public void setAppStateModel(AppState appStateModel) {
		this.appStateModel = appStateModel;
	}

	private static final Logger logger = LoggerFactory.getLogger(CategoryUIController.class);
	
	
	private BankStatementRow selectedRow;
	
	private String selection;

	public String getSelection() {
		return selection;
	}

	@FXML
	private BorderPane borderPane;
	@FXML
	private ListView<String> listCategory;
	
	@FXML
	private ListView<String> listDetail;
	
	@FXML
	private Button okButton;
	
	@FXML
	private Button addButton;
	
	@FXML
	private TextField newCatText;
	
	@FXML
	private Button addMatchButton;
	
	
	@FXML
	private TextField matchText;
	
	private Category selectedCategory;
	
	

	@FXML
	public void initialize() {
		logger.info("intitializing catcontroler="+this.toString());
		
				
		names = FXCollections.observableArrayList();
		matches = FXCollections.observableArrayList();	
		
		listCategory.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				logger.info(newValue);
				selection = newValue;
				selectedCategory = categories.getCategoryByName(selection);
				if (selectedCategory != null)
					loadMatches(selectedCategory);

			}
		});
	}

	@FXML
	public void onOKClick(ActionEvent event) {
	
		appStateModel.setBankStatementRowChanged(true);
		Stage stage = (Stage) borderPane.getScene().getWindow();
		stage.hide();
 
	}
	
	@FXML
	public void onAddClick(ActionEvent event) {
		logger.info("onAddClick");
		Category cat = new Category();
		cat.setName(newCatText.getText());
		categories.add(cat);
		loadCategories();
	}
	
	
	@FXML
	public void onAddMatch(ActionEvent event) {
		logger.info("onAddMatch");
		selectedCategory.getDescriptionMatches().add(matchText.getText());
		logger.debug(selectedCategory.getDescriptionMatches().toString());
		loadMatches(selectedCategory) ;	
	}
	
	private void loadMatches(Category cat) {
		matches.clear();
		
		List<String> theMatches = cat.getDescriptionMatches();
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

		listCategory.setItems(names);
		
	}


	public CategoryUIController() {
		logger.info("constructing catcontroler="+this.toString());
		
	}
	
	
	public void show(BankStatementRow selectedRow) {
	
		Stage stage = (Stage) borderPane.getScene().getWindow();
		loadCategories();
		newCatText.clear();
		this.selectedRow = selectedRow;
		stage.show();
		stage.toFront();
		
	}
	
}
