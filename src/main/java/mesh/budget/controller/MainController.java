package mesh.budget.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import mesh.budget.Budget1Application;
import mesh.budget.Utils;
import mesh.budget.model.BankStatementRow;
import mesh.budget.model.Budget;

public class MainController {

	private static final Logger logger = LoggerFactory.getLogger(MainController.class);
	static String budgetFileName = "C:\\Users\\patri\\git\\budget\\budget.csv";
	public CategoryUIController catController;
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
		showCategoryManger();

	}

	@FXML
	public void initialize() {
		tableSetup();
		loadData();
		
	}

	

	private void loadData() {
		ObservableList<BankStatementRow> l = FXCollections.observableArrayList();

		Budget budget = new Budget();
		budget.loadFromFile(budgetFileName);
		// add exports

		List<File> exports = Utils.findExports("C:\\Users\\patri\\Downloads");

		for (File e : exports) {
			budget.addExportFile(e.getAbsolutePath());

		}

		budget.saveToFile(budgetFileName);

		for (BankStatementRow r : budget.getBudget()) {
			l.add(r);

		}

		table1.setItems(l);

	}

	

	private void tableSetup() {
		if (!tableCreated) {

			TableColumn dateProcessed = new TableColumn("dateProcessed");
			TableColumn dateOfTransaction = new TableColumn("dateOfTransaction");
			TableColumn id = new TableColumn("id");
			TableColumn type = new TableColumn("type");
			TableColumn referenece = new TableColumn("reference");
			TableColumn description = new TableColumn("description");
			TableColumn amount = new TableColumn("amount");
			TableColumn category = new TableColumn("category");

			table1.getColumns().addAll(dateProcessed, dateOfTransaction, id, type, referenece, description, amount,
					category);

			dateProcessed.setCellValueFactory(new PropertyValueFactory<BankStatementRow, String>("dateProcessed"));
			amount.setCellValueFactory(new PropertyValueFactory<BankStatementRow, String>("amount"));
			description.setCellValueFactory(new PropertyValueFactory<BankStatementRow, String>("description"));
			dateOfTransaction
					.setCellValueFactory(new PropertyValueFactory<BankStatementRow, String>("dateOfTransaction"));
			id.setCellValueFactory(new PropertyValueFactory<BankStatementRow, String>("id"));
			referenece.setCellValueFactory(new PropertyValueFactory<BankStatementRow, String>("reference"));
			type.setCellValueFactory(new PropertyValueFactory<BankStatementRow, String>("type"));
			category.setCellValueFactory(new PropertyValueFactory<BankStatementRow, String>("category"));

			tableCreated = true;
		}
	}

	@FXML
	public void loadData(ActionEvent event) {

		logger.info("action event");
	}

	@FXML
	public void HelloButtonClicked(ActionEvent event) {

		logger.info("hello button clicked");
		table1.refresh();

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
	
	public MainController() {
		logger.info("constructing main controler="+this.toString());
		
	}
}
