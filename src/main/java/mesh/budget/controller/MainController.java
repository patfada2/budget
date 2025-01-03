package mesh.budget.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import mesh.budget.Utils;
import mesh.budget.model.AppState;
import mesh.budget.model.BankStatementRow;
import mesh.budget.model.Budget;
import mesh.budget.model.Categories;
import mesh.budget.model.Category;

public class MainController {

	private static final Logger logger = LoggerFactory.getLogger(MainController.class);
	
	
	//Models
	private AppState appStateModel;
	private Categories categories;
	private Budget budget;
	

	public CategoryUIController catController;
	public AddMatchController addDescriptionMatchController;
	public AddMatchController addReferenceMatchController;

	
	
	
	public Scene catScene;
	public Stage catStage;

	@FXML
	private Button labeutton1l1;
	@FXML
	private TableView table1;
	@FXML
	private DialogPane dialogPane1;

	@FXML
	private Alert alert;

	private boolean tableCreated = false;

	@FXML
	public void Table1Context(ContextMenuEvent event) {
		// System.out.println("context");
		logger.info("context");
		
	}
	
	@FXML
	public void loadExports(ActionEvent event) {

		logger.info("loading exports");
		budget.loadExports();
	}
	
	@FXML
	public void save(ActionEvent event) {
		budget.saveToFile(Utils.budgetFileName);
		categories.saveToFile(Utils.categoryFileName);		
	}
	
	@FXML
	public void showCategoryManager(ActionEvent event) {
		logger.info("showing Category Manger");
		showCategoryManger();
	}
	
	@FXML
	public void runMatches(ActionEvent event) {
		logger.info("running matches");
		budget.runMatches(categories);
		table1.refresh();
	}
	
	
	

	@FXML
	public void HelloButtonClicked(ActionEvent event) {

		logger.info("hello button clicked");
		table1.refresh();

	}


	@FXML
	public void initialize() {
		tableSetup();
	
		appStateModel.bankStatementRowChangedProperty().addListener((observable, oldValue, newValue) -> {
            // Only if completed
          logger.info("bankStatementRow changed");
          table1.refresh();
          appStateModel.setBankStatementRowChanged(false);
        });
			
		budget.getBudget().addListener(new ListChangeListener<BankStatementRow>() {
			  @Override
			  public void onChanged(Change<? extends BankStatementRow> c) {
				  table1.refresh();
				  logger.info("budget changed");
			  }
			});

	}



	private void tableSetup() {
		if (!tableCreated) {

			TableColumn<BankStatementRow, String> dateProcessed = new TableColumn<BankStatementRow, String>("dateProcessed");
			TableColumn<BankStatementRow, String> dateOfTransaction = new TableColumn<BankStatementRow, String>("dateOfTransaction");
			TableColumn<BankStatementRow, String> id = new TableColumn<BankStatementRow, String>("id");
			TableColumn<BankStatementRow, String> type = new TableColumn<BankStatementRow, String>("type");
			TableColumn<BankStatementRow, String> reference = new TableColumn<BankStatementRow, String>("reference");
			TableColumn<BankStatementRow, String> description = new TableColumn<BankStatementRow, String>("description");
			TableColumn<BankStatementRow, String> amount = new TableColumn<BankStatementRow, String>("amount");
			TableColumn<BankStatementRow, String> category = new TableColumn<BankStatementRow, String>("category");

			table1.getColumns().addAll(dateProcessed, dateOfTransaction, id, type, reference, description, amount,
					category);

			dateProcessed.setCellValueFactory(new PropertyValueFactory<BankStatementRow, String>("dateProcessed"));
			amount.setCellValueFactory(new PropertyValueFactory<BankStatementRow, String>("amount"));
			description.setCellValueFactory(new PropertyValueFactory<BankStatementRow, String>("description"));
			reference.setCellValueFactory(new PropertyValueFactory<BankStatementRow, String>("reference"));
			
			
			description.setOnEditStart(event -> {
			    String currentValue = event.getOldValue();
			   
			    BankStatementRow row = (BankStatementRow)event.getRowValue();
			    addDescriptionMatchController.show(currentValue,row);
			});
			
			reference.setOnEditStart(event -> {
			    String currentValue = event.getOldValue();
			   
			    BankStatementRow row = (BankStatementRow)event.getRowValue();
			    addReferenceMatchController.show(currentValue,row);
			});
			
			
			dateOfTransaction
					.setCellValueFactory(new PropertyValueFactory<BankStatementRow, String>("dateOfTransaction"));
			id.setCellValueFactory(new PropertyValueFactory<BankStatementRow, String>("id"));
			reference.setCellValueFactory(new PropertyValueFactory<BankStatementRow, String>("reference"));
			type.setCellValueFactory(new PropertyValueFactory<BankStatementRow, String>("type"));
		
			category.setCellValueFactory(new PropertyValueFactory<BankStatementRow,String>("category"));
			category.setCellFactory(column -> new TableCel_Edit());
			
			
			
			
			};
						

			tableCreated = true;
			table1.setItems(budget.getBudget());
			table1.setEditable(true);
			
		}
	
	
	
	 private class TableCel_Edit extends TableCell<BankStatementRow, String> {

	        ChoiceBox<String> categoryBox = new ChoiceBox<>();

	        public TableCel_Edit() {
	        	
	        	for (Category c : categories.getCategories()) {
	        		categoryBox.getItems().add(c.getName());
	    		}
	        	
	        	categoryBox.getSelectionModel().selectedIndexProperty().addListener((obs, oldValue, newValue) -> {

	                String value = categoryBox.getItems().get(newValue.intValue());
	                processEdit(value);
	            });

	        }

	        private void processEdit(String value) {
	            commitEdit(value);
	        }

	        @Override
	        public void cancelEdit() {
	            super.cancelEdit();
	            setText(getItem());
	            setGraphic(null);
	        }

	        @Override
	        public void commitEdit(String value) {
	            super.commitEdit(value);
	            TableRow<BankStatementRow> row = this.getTableRow();
	            row.getItem().setCategory(value);	           
	            setGraphic(null);
	            table1.refresh();
	            
	        }

	        @Override
	        public void startEdit() {
	            super.startEdit();
	            String value = getItem();
	            if (value != null) {
	                setGraphic(categoryBox);
	                setText(null);
	            }
	        }

	        @Override
	        protected void updateItem(String item, boolean empty) {
	            super.updateItem(item, empty);
	            if (item == null || empty) {
	                setText(null);

	            } else {
	                setText(item);
	            }
	        }

	    }

	
	private void showCategoryManger() {

		
		BankStatementRow r = (BankStatementRow) table1.getSelectionModel().getSelectedItem();

		//catController.setSelectedRow(r);

		if (r == null) {
			logger.error("selected row is null");
		} else {
			logger.info("showing category manager for " + r.getDescription());
		}
		catController.show(r);

	}

	// Event Listener on TableView.onMouseClicked
	@FXML
	public void Table1Clicked(MouseEvent event) {
		logger.info("Table1Clicked");

		table1.refresh();

	}

	private URL getResourceURL(final String fileName) {
		URL url = this.getClass().getClassLoader().getResource(fileName);

		if (url == null) {
			throw new IllegalArgumentException(fileName + " is not found 1");
		}

		return url;
	}
	
	public MainController(AppState appStateModel,Categories categories, Budget budget)
	{
		this.appStateModel=appStateModel;
		this.categories = categories;
		this.budget = budget;
		logger.info("constructing main controler="+this.toString());
		
	}
}
