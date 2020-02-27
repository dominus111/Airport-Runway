package runway.Controller;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.ContextMenuEvent;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Controller {

    @FXML
    void sideOnTabEvent(Event event) {

    }

    @FXML
    void topDownTabEvent(Event event) {

    }

    // Opens up the Runway Creation Window
    @FXML
    void addRunwayButtonEvent(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        URL url = new File("src/main/resources/fxml/RunwayCreation.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(url);
        stage.setTitle("Runway Creation");
        stage.setResizable(false);
        stage.setScene(new Scene(root, 296, 540));
        stage.show();
    }

    @FXML
    void removeRunwayButtonEvent(ActionEvent event) {

    }
    @FXML
    void runwaySelectEvent(ContextMenuEvent event) {

    }

    // Opens up the Object window
    @FXML
    void addObjectToRunwayEvent(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        URL url = new File("src/main/resources/fxml/ObjectCreation.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(url);
        stage.setTitle("Object");
        stage.setResizable(false);
        stage.setScene(new Scene(root, 216, 521));
        stage.show();
    }

}
