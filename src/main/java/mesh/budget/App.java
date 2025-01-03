package mesh.budget;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mesh.budget.controller.AddMatchController;
import mesh.budget.controller.AddMatchController.MatchTypes;
import mesh.budget.controller.CategoryUIController;
import mesh.budget.controller.MainController;
import mesh.budget.model.AppState;
import mesh.budget.model.Budget;
import mesh.budget.model.Categories;
import mesh.budget.model.Category;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;

/**
 * JavaFX App
 */
public class App extends Application {

	private static final Logger logger = LoggerFactory.getLogger(App.class);

	private CategoryUIController catController;
	private AddMatchController addReferenceMatchController;
	private AddMatchController addDescriptionMatchController;
	private AppState appStateModel = new AppState();
	private Categories categories;
	private Budget budget = new Budget();

	@Override
	public void start(Stage stage) {
		
		//initialise model
		loadBudget();
		
		//initialise views
		categoryManagerSetup();
		addReferenceMatchController = addMatchSetup(MatchTypes.REFERNCE);
		addDescriptionMatchController = addMatchSetup(MatchTypes.DESCRIPTION);
		mainSetup(stage);
		
		

	}

	private void loadBudget() {
		budget.loadFromFile(Utils.budgetFileName);
	
	}

	private AddMatchController addMatchSetup(MatchTypes matchType) {
		URL location = getResourceURL("fxml/AddMatch.fxml");
		Scene scene;
		Stage stage;

		Parent root;

		AddMatchController matchController = new AddMatchController(budget,categories,matchType);
		FXMLLoader addMatchLoader = new FXMLLoader(location);
		addMatchLoader.setController(matchController);

		try {
			root = addMatchLoader.load();
			scene = new Scene(root);
			stage = new Stage();
			stage.initModality(Modality.WINDOW_MODAL);
			stage.setScene(scene);

		} catch (IOException e) {
			logger.info("categoryManagerSetup failed");
			e.printStackTrace();

		}
		return matchController;

	}

	private void mainSetup(Stage stage) {
		try {

			logger.info(appStateModel.toString());
			URL location = getResourceURL("fxml/mainUI.fxml");
			MainController mainController = new MainController(appStateModel, categories, budget);

			FXMLLoader mainLoader = new FXMLLoader(location);
			mainLoader.setController(mainController);

			Parent root = mainLoader.load();

			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();

			mainController.catController = this.catController;
			mainController.addDescriptionMatchController = this.addDescriptionMatchController;
			mainController.addReferenceMatchController = this.addReferenceMatchController;

			logger.info(appStateModel.toString());


		} catch (IOException e) {
			logger.info("App.start() failed");
			e.printStackTrace();

		}

	}

	private void categoryManagerSetup() {
		Scene catScene;
		Stage catStagex;
		URL location = getResourceURL("fxml/CategoryUI.fxml");

		Parent root;

		FXMLLoader catLoader = new FXMLLoader(location);
		catController = new CategoryUIController();
		catLoader.setController(catController);
		catController.setAppStateModel(appStateModel);

		categories = new Categories();
		categories.loadFromFile(Utils.categoryFileName);
						
		catController.setCategories(categories);
		
		categories.saveToFile(Utils.categoryFileName+"_xxx");
		
		logger.info("catcontroler=" + catController.toString());
		try {
			root = catLoader.load();
			catScene = new Scene(root);
			catStagex = new Stage();
			catStagex.setScene(catScene);

		} catch (IOException e) {
			logger.info("categoryManagerSetup failed");
			e.printStackTrace();

		}
	}

	private URL getResourceURL(final String fileName) {
		URL url = this.getClass().getClassLoader().getResource(fileName);

		if (url == null) {
			throw new IllegalArgumentException(fileName + " is not found 1");
		}

		return url;
	}

	public static void main(String[] args) {
		launch();
	}

}
