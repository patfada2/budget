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
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import mesh.budget.model.AppState;
import mesh.budget.model.BankStatementRow;
import mesh.budget.model.Budget;
import mesh.budget.model.Categories;
import mesh.budget.model.Category;

public class CategoryUIController {

	private Categories categories;
	private Budget budget;
	private AppState appStateModel;

	public void setCategories(Categories categories) {
		this.categories = categories;
	}

	private ObservableList<String> names;
	private ObservableList<String> descMatches;
	private ObservableList<String> refMatches;

	public void setBudget(Budget budget) {
		this.budget = budget;
	}

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
	private AnchorPane catDetailAnchor;
	
	@FXML
	private Rectangle catColourRect;
	
	@FXML
	private BorderPane borderPane;
	@FXML
	private ListView<String> listCategory;

	@FXML
	private ListView<String> descriptionList;

	@FXML
	private ListView<String> referenceList;

	@FXML
	private Button okButton;

	@FXML
	private Button addButton;

	@FXML
	private TextField newCatText;

	@FXML
	private Button addMatchButton;

	@FXML
	private Button deleteCatButton;

	@FXML
	private TextField descriptionMatchText;

	@FXML
	private TextField referenceMatchText;
	
	@FXML
	private TextField budgetText;

	private Category selectedCategory;

	@FXML
	public void initialize() {
		logger.info("intitializing catcontroler=" + this.toString());

		names = FXCollections.observableArrayList();
		descMatches = FXCollections.observableArrayList();
		refMatches = FXCollections.observableArrayList();

		listCategory.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				logger.info(newValue);
				selection = newValue;
				selectedCategory = categories.getCategoryByName(selection);
				if (selectedCategory != null) {
					loadMatches(selectedCategory);
					budgetText.setText(String.valueOf(selectedCategory.getBudget()));
					String colourString = categories.getDefaultCatColour((selectedCategory.getName()));
					String s = colourString.substring(colourString.indexOf(":")+1,colourString.length()-1);
					logger.debug("colour:"+s);
					
					Color c = Color.web(s.trim());
					catColourRect.setFill(c);
					reDraw();
				}
			}
		});

		listCategory.setEditable(true);
		listCategory.setCellFactory(TextFieldListCell.forListView());
		listCategory.setOnEditCommit(event -> {
			String newValue = event.getNewValue();
			String oldValue = selectedCategory.getName();

			logger.info("category renamed from " + oldValue + " to " + newValue);
			if (!newValue.equals(oldValue)) {

				budget.renameCategories(oldValue, newValue);
				selectedCategory.setName(newValue);
				loadCategories();
			}

		});

	}

	@FXML
	public void onOKClick(ActionEvent event) {

		appStateModel.setBankStatementRowChanged(true);
		Stage stage = (Stage) borderPane.getScene().getWindow();
		stage.hide();

	}
	
	private void reDraw() {
		Stage stage = (Stage) borderPane.getScene().getWindow();
		stage.show();
		
	}

	@FXML
	public void onAddCategoryClick(ActionEvent event) {
		logger.info("onAddClick");
		Category cat = new Category();
		String name = newCatText.getText();
		cat.setName(name.replace(',', ' '));
		categories.add(cat);
		loadCategories();
	}

	// deleteCatButton

	@FXML
	public void onDeleteCat(ActionEvent event) {
		logger.info("onDeleteCat");

		categories.delete(selectedCategory.getName());
		loadCategories();
	}

	@FXML
	public void onDeleteDescMatch(ActionEvent event) {
		logger.info("ondeleteDescMatch");
		String match = descriptionList.getSelectionModel().getSelectedItem();
		selectedCategory.deleteDescriptionMatch(match);
		loadMatches(selectedCategory);
	}

	// onDeleteRefMatch
	@FXML
	public void onDeleteRefMatch(ActionEvent event) {
		logger.info("onDeleteRefMatch");
		String match = referenceList.getSelectionModel().getSelectedItem();
		selectedCategory.deleteReferenceMatch(match);
		loadMatches(selectedCategory);
	}

	@FXML
	public void onAddDescriptionMatch(ActionEvent event) {
		logger.info("onAddDescriptionMatch");
		selectedCategory.getDescriptionMatches().add(descriptionMatchText.getText());
		logger.debug(selectedCategory.getDescriptionMatches().toString());
		loadMatches(selectedCategory);
	}

	@FXML
	public void onAddReferenceMatch(ActionEvent event) {
		logger.info("onAddReferenceMatch");
		selectedCategory.getReferenceMatches().add(referenceMatchText.getText());
		logger.debug(selectedCategory.getReferenceMatches().toString());
		loadMatches(selectedCategory);
	}

	@FXML
	public void onSetBudget(ActionEvent event) {
		selectedCategory.setBudget(Double.parseDouble(budgetText.getText()));
		
	}
	
	private void loadMatches(Category cat) {
		logger.info("loading matches for cat " + cat.getName());
		descMatches.clear();

		List<String> descriptionMatches = cat.getDescriptionMatches();
		for (int i = 0; i < descriptionMatches.size(); i++) {
			descMatches.add(descriptionMatches.get(i));
		}
		descriptionList.setItems(descMatches);

		refMatches.clear();

		List<String> referenceMatches = cat.getReferenceMatches();
		for (int i = 0; i < referenceMatches.size(); i++) {
			refMatches.add(referenceMatches.get(i));
		}
		referenceList.setItems(refMatches);

	}

	private void loadCategories() {
		names.clear();

		for (Category c : categories.getCategories()) {
			names.add(c.getName());
		}

		listCategory.setItems(names);

	}

	public CategoryUIController() {
		logger.info("constructing catcontroler=" + this.toString());

	}

	public void show(BankStatementRow selectedRow) {

		Stage stage = (Stage) borderPane.getScene().getWindow();
		loadCategories();
		listCategory.getSelectionModel().select(0);

		newCatText.clear();
		this.selectedRow = selectedRow;
		stage.show();
		stage.toFront();

	}

}
