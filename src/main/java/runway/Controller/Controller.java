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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    private AnchorPane topView;

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

    public ComboBox<String> getRunwaySelect() {
        return runwaySelect;
    }

    public Airport getAirport() {
        return airport;
    }

    @FXML
    void sideOnTabEvent(Event event) {

    }

    @FXML
    void topDownTabEvent(Event event) {
        Image image = new Image ("file:runaway.png");
        ImageView view = new ImageView();
        view.setImage(image);

        topView.getChildren().add(view);

    }

    // Opens up the Runway Creation Window
    @FXML
    void addRunwayButtonEvent(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/RunwayCreation.fxml"));
            Parent root = loader.load();
            RunwayCreationController ctrl = loader.getController();

            ctrl.setParentController(this);
            Stage stage = new Stage();
            stage.setTitle("Runway Creation");
            stage.setResizable(false);
            stage.setScene(new Scene(root, 295, 585));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    void addObjectToRunwayEvent(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ObjectCreation.fxml"));
            Parent root = loader.load();
            ObjectCreationController ctrl = loader.getController();

            ctrl.setParentController(this);
            Stage stage = new Stage();
            stage.setTitle("Object");
            stage.setResizable(false);
            stage.setScene(new Scene(root, 256, 556));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
