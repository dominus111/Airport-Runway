package runway.View;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import runway.Controller.Controller;
import runway.Model.Airport;
import runway.Test;

import java.io.File;
import java.net.URL;

public class GUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Scheleton.fxml"));
            Parent root = loader.load();
            Controller controller = new Controller();
            loader.setController(controller);
            primaryStage.setTitle("Runway Redeclaration Tool");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
            primaryStage.setResizable(true);
            controller.initialize();
            Test test = new Test();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
