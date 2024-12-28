package application;
	
import java.sql.Connection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application{
	@Override
	public void start(Stage primaryStage) {
		try {
//			BorderPane root = new BorderPane();
//			Scene scene = new Scene(root,400,400);
//			scene.getStylesheets().add(getClass().getResource("Login.fxml").toExternalForm());
			
			Parent root = FXMLLoader.load(getClass().getResource("/ui_final/Login.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
            primaryStage.setTitle("Login Form");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);

	}
}
