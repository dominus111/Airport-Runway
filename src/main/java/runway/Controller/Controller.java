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
    @FXML
    void addRunwayButtonEvent(ActionEvent event) {

    }

    @FXML
    void removeRunwayButtonEvent(ActionEvent event) {

    }
    @FXML
    void runwaySelectEvent(ContextMenuEvent event) {

    }

    @FXML
    void addObjectToRunwayEvent(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        URL url = new File("src/main/resources/fxml/ObjectCreation.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(url);
        stage.setTitle("Object Creation");
        stage.setScene(new Scene(root, 624, 540));
        stage.show();
    }

}
