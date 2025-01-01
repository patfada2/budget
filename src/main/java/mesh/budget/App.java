package mesh.budget;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
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
import javafx.stage.Stage;
import mesh.budget.controller.CategoryUIController;
import mesh.budget.controller.MainController;
import mesh.budget.model.AppState;
import mesh.budget.model.Budget;
import mesh.budget.model.Categories;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;

/**
 * JavaFX App
 */
public class App extends Application {

	private static final Logger logger = LoggerFactory.getLogger(App.class);
	

	private Scene catScene;
	private Stage catStagex;
	private CategoryUIController catController;
	private AppState appStateModel = new AppState();
	private Categories categories;
	private Budget budget = new Budget();

	@Override
	public void start(Stage stage) {

		categoryManagerSetup();
		logger.info("1.cat stagex =" + this.catStagex);
		loadBudget();

		try {

			logger.info(appStateModel.toString());
			URL location = getResourceURL("fxml/mainUI.fxml");
			MainController mainController = new MainController(appStateModel, categories,budget);

			FXMLLoader mainLoader = new FXMLLoader(location);
			mainLoader.setController(mainController);

			Parent root = mainLoader.load();

			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();

			// mainController.catScene = this.catScene;
			// mainController.catStage = this.catStage;
			mainController.catController = this.catController;

			logger.info(appStateModel.toString());

			logger.info("2.cat stagex =" + this.catStagex);

		} catch (IOException e) {
			logger.info("App.start() failed");
			e.printStackTrace();

		}
	}

	private void loadBudget() {
		budget.loadFromFile(Utils.budgetFileName);
		// add exports

		List<File> exports = Utils.findExports("C:\\Users\\patri\\Downloads");

		for (File e : exports) {
			budget.addExportFile(e.getAbsolutePath());

		}

		budget.saveToFile(Utils.budgetFileName);
	}

	private void categoryManagerSetup() {
		URL location = getResourceURL("fxml/CategoryUI.fxml");

		Parent root;

		FXMLLoader catLoader = new FXMLLoader(location);
		catController = new CategoryUIController();
		catLoader.setController(catController);
		catController.setAppStateModel(appStateModel);

		categories = new Categories();
		categories.loadFromFile(Utils.categoryFileName);
		catController.setCategories(categories);

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
