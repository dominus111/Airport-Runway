package runway.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import runway.Model.Airport;
import runway.Model.Runway;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ListIterator;

public class Controller {

    @FXML
    private ComboBox<String> runwaySelect;
    private Airport airport;
    @FXML
    private AnchorPane jacobAnchorPane;

    @FXML
    public void initialize() {
        airport = new Airport();

        if(runwaySelect != null) {
            runwaySelect.getItems().clear();
            for(Runway currentRunway : airport.getObservableRunwayList()) {
                runwaySelect.getItems().add(currentRunway.toString());
            }
        }
    }

    public void setAirport(Airport airport) {
        this.airport = airport;
    }

    @FXML
    void sideOnTabEvent(Event event) {

    }

    @FXML
    void topDownTabEvent(Event event) {

    }

    // Opens up the Runway Creation Window
    @FXML
    void addRunwayButtonEvent(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        Stage stage = new Stage();
        URL url = new File("src/main/resources/fxml/RunwayCreation.fxml").toURI().toURL();
        loader.setLocation(url);
        RunwayCreationController controller = new RunwayCreationController();
        controller.setAirport(airport);
        loader.setController(controller);

        Parent root = FXMLLoader.load(url);
        stage.setTitle("Runway Creation");
        stage.setResizable(false);
        stage.setScene(new Scene(root, 295, 585));
        stage.show();
    }

    @FXML
    void removeRunwayButtonEvent(ActionEvent event) {

        // Removes the runway that is selected
        if (runwaySelect.getSelectionModel().getSelectedItem() != null) {
            ObservableList<Runway> observableNewList = FXCollections.observableArrayList();
            String runwaySelected = runwaySelect.getSelectionModel().getSelectedItem();
            runwaySelect.getItems().clear();

            for (Runway currentRunway : airport.getObservableRunwayList()) {
                if (!runwaySelected.equals(currentRunway.toString())) {
                    observableNewList.add(currentRunway);
                    runwaySelect.getItems().add(currentRunway.toString());
                }
            }

            airport.setObservableRunwayList(observableNewList);
        }
    }

    @FXML
    void runwaySelectEvent(Event event) {

    }

    // Opens up the Object window
    @FXML
    void addObjectToRunwayEvent(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        URL url = new File("src/main/resources/fxml/ObjectCreation.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(url);
        stage.setTitle("Object");
        stage.setResizable(false);
        stage.setScene(new Scene(root, 256, 556));
        stage.show();
    }
}
