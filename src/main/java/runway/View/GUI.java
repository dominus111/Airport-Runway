package runway.View;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import runway.Controller.Controller;
import runway.Model.Airport;

import java.io.File;
import java.net.URL;

public class GUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader();
        URL url = new File("src/main/resources/fxml/Scheleton.fxml").toURI().toURL();
        loader.setLocation(url);
        Controller controller = new Controller();
        loader.setController(controller);

        Parent root = FXMLLoader.load(url);
        primaryStage.setTitle("Runway Redeclaration Tool");
        primaryStage.setScene(new Scene(root, 1024, 740));
        primaryStage.show();
        controller.initialize();
    }
}
