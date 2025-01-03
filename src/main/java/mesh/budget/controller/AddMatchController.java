package mesh.budget.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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

	public enum MatchTypes {
		DESCRIPTION, REFERNCE
	};

	private MatchTypes matchType;

	private BankStatementRow selectedRow;

	@FXML
	private AnchorPane anchorPane;

	@FXML
	private TextField matchText;

	// Event Listener on Button.onAction
	@FXML
	public void addMatch(ActionEvent event) {
		String match = matchText.getText();

		logger.info("adding match " + match + " to category:" + selectedRow.getCategory());

		if (selectedRow.getCategory().equals(Category.UNKNOWN)) {
			showAlert();
		} else {

			Category selectedCategory = categories.getCategoryByName(selectedRow.getCategory());
			switch (matchType) {
			case MatchTypes.DESCRIPTION:
				selectedCategory.getDescriptionMatches().add(match);
			case MatchTypes.REFERNCE:
				selectedCategory.getReferenceMatches().add(match);

			}

		}
		hide();

	}

	private void showAlert() {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Message");
		alert.setHeaderText("cant add match before category is assigned");
		alert.showAndWait();
	}

	public AddMatchController(Budget budget, Categories categories, MatchTypes matchType) {
		this.budget = budget;
		this.categories = categories;
		this.matchType = matchType;
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
		stage.toFront();
	}

}
