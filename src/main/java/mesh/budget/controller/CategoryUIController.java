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
import javafx.geometry.Insets;
import javafx.scene.control.Button;

import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableRow;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import mesh.budget.Utils;
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
	private ComboBox<Item> colourPickComboBox;

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
					Color c = Utils.cssStringToColor(selectedCategory.getColour());
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
		for (int i = 0; i < Categories.catColours.length; i++) {
			MyText text = new MyText(Categories.catColours[i]);
			Item item = new Item(Categories.catColours[i], Utils.cssStringToColor(Categories.catColours[i]));
			
			colourPickComboBox.getItems().add(item);
			
		}
		colourPickComboBox.setCellFactory(lv -> new ListCell<Item>(){

		    @Override
		    protected void updateItem(Item item, boolean empty) {
		        super.updateItem(item, empty);

		        if (empty || item == null) {
		            setBackground(Background.EMPTY);
		            setText("");
		        } else {
		            setBackground(new Background(new BackgroundFill(item.getColor(),
		                                                            CornerRadii.EMPTY,
		                                                            Insets.EMPTY)));
		            setText(item.getValue());
		        }
		    }

		});


	}
	
	private class Item {

	    public Item(String value, Color color) {
	        this.value = value;
	        this.color = color;
	    }

	    private final String value;
	    private final Color color;

	    public String getValue() {
	        return value;
	    }

	    public Color getColor() {
	        return color;
	    }
	}

	private class MyText extends Text{
		
		public MyText(String s) {
			super(s);
			DropShadow ds = new DropShadow();
			ds.setOffsetY(3.0f);
			Color c = Utils.cssStringToColor(s);
			ds.setColor(c);
			this.setEffect(ds);	
			logger.info(this.getText() + "%%%%" + this.toString());
		}
		
		public String toString() {
			return this.getText();
		}
		
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

	@FXML
	public void onSelectColour(MouseEvent event) {

		logger.info("onColourPick");
		
		colourPickComboBox.getSelectionModel().selectedIndexProperty().addListener((obs, oldValue, newValue) -> {
			
			Item t = colourPickComboBox.getItems().get(newValue.intValue());
			
			Color c = t.color;
			logger.info("colour " + c.toString() + " selected");
			catColourRect.setFill(c);
			selectedCategory.setColour(c);
			reDraw();
			// processEdit(value);
		});

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

		
		loadCategories();
		listCategory.getSelectionModel().select(0);

		newCatText.clear();
		this.selectedRow = selectedRow;
		
		Stage stage = (Stage) borderPane.getScene().getWindow();
		
		stage.show();
		stage.toFront();

	}

}
