package mesh.budget.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import mesh.budget.model.BankStatementRow;
import mesh.budget.model.Budget;
import mesh.budget.model.Categories;
import mesh.budget.model.Category;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.event.ActionEvent;

public class AddMatchController {

	private Budget budget;
	private Categories categories;
	private static final Logger logger = LoggerFactory.getLogger(AddMatchController.class);
	
	private BankStatementRow selectedRow;

	@FXML
	private AnchorPane anchorPane;

	@FXML
	private TextField matchText;

	// Event Listener on Button.onAction
	@FXML
	public void addMatch(ActionEvent event) {
		
		logger.info("adding match to category:"+selectedRow.getCategory());
		
		if (selectedRow.getCategory().equals(Category.UNKOWN)) {
			logger.info(" cant add match befroe categtoy is assigned");
		}
		else {
			
			Category selectedCategory = categories.getCategoryByName(selectedRow.getCategory());
			
			selectedCategory.getMatches().add(matchText.getText());
		}
		hide();
			
	}

	public AddMatchController(Budget budget, Categories categories) {
		this.budget = budget;
		this.categories=categories;
	}
	
	private void hide() {
		Stage stage = (Stage) anchorPane.getScene().getWindow();
		stage.hide();		
	}

	public void show(String description, BankStatementRow selectedRow) {

		Stage stage = (Stage) anchorPane.getScene().getWindow();
		this.selectedRow = selectedRow;
		matchText.setText(description);

		stage.show();
	}

}
