package mesh.budget;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;

/**
 * JavaFX App
 */
public class App extends Application {

	@Override
	public void start(Stage stage) {
		
		try {
			
			URL location = getResourceURL("fxml/mainUI.fxml");
			
			Parent root = FXMLLoader.load(location);
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private URL getResourceURL(final String fileName) 
	{
	    URL url = this.getClass()
	        .getClassLoader()
	        .getResource(fileName);
	    
	    if(url == null) {
	        throw new IllegalArgumentException(fileName + " is not found 1");
	    }
	    
	    
	    return url;
	}
	
	private InputStream getFileAsIOStream(final String fileName) 
	{
	    InputStream ioStream = this.getClass()
	        .getClassLoader()
	        .getResourceAsStream(fileName);
	    
	    if (ioStream == null) {
	        throw new IllegalArgumentException(fileName + " is not found");
	    }
	    return ioStream;
	}
	
	public static void main(String[] args) {
		launch();
	}
	

}