package test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/gui.fxml"));
		Parent parent = loader.load();
		Controller controller = loader.getController();
		primaryStage.setTitle("Testy");
		primaryStage.setScene(new Scene(parent));
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
