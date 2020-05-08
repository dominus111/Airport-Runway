package runway.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import runway.Model.Calculator;
import runway.Model.Obstacle;
import runway.Model.ObstaclePositionParam;
import runway.Model.Runway;

public class ObjectCreationController {
    @FXML
    private Button cancelButton;
    private Controller parentController;
    @FXML
    private TextField nameTextField;

    @FXML
    private TextField heightTextField;

    @FXML
    private Label rightThrLabel, leftThrLabel;

    @FXML
    private TextField distToCLTextField;

    @FXML
    private TextField distToThrTextField;

    @FXML
    private TextField bottomDistToThrTextF;

    @FXML
    public ComboBox<Obstacle> objectComboBox;


    @FXML
    void objectCancelButtonEvent(ActionEvent event) {
        parentController.setAllButtonsDisable(false);
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public Label getRightThrLabel() {
        return rightThrLabel;
    }

    public Label getLeftThrLabel() {
        return leftThrLabel;
    }

    public void setParentController(Controller parentController) {
        this.parentController = parentController;
    }

    @FXML
    void assignButtonEvent(ActionEvent event) {

        Boolean disable = false;

        if (parentController.getRunwaySelect().getSelectionModel().getSelectedItem() == null) {
            errorWindow("No runway is selected.\nPlease select one and try again.");

        } else {
            Runway runway = parentController.getAirport().getRunway(parentController.getRunwaySelect().getSelectionModel().getSelectedItem());
            if (nameTextField.getText() == null || heightTextField.getText() == null || distToCLTextField.getText() == null || distToThrTextField == null || bottomDistToThrTextF == null)
                errorWindow("Please complete all fields");

            try {
                String name = nameTextField.getText();
                double height = Double.parseDouble(heightTextField.getText());
                double distToCenterL = Double.parseDouble(distToCLTextField.getText());
                double distToThr = Double.parseDouble(distToThrTextField.getText());
                double distToOtherThr = Double.parseDouble(bottomDistToThrTextF.getText());

                Obstacle obstacle = new Obstacle(name, height);
                ObstaclePositionParam oParam;
                oParam = new ObstaclePositionParam(obstacle, distToThr, distToOtherThr, distToCenterL);

                obstacle.setoParam(oParam);
                Calculator calculator = new Calculator();
                calculator.calculate(oParam, runway);
                parentController.updateTables();
                parentController.notify("Object " + name + " added to " + runway);

                runway.setObstacle(obstacle);
                parentController.runwayUpdate();
                parentController.topRunwayUpdate();
                objectCancelButtonEvent(event);

                parentController.removeObjButton.setDisable(false);
                parentController.removeObjButton.setText("Remove Object " + name);

            } catch (NullPointerException | NumberFormatException ex) {
                errorWindow("Text field for numbers contains letters or is empty.");
                disable = true;
            }
        }

        if (!disable) {
            parentController.setAllButtonsDisable(false);
        }
     }

    @FXML
    void objectSelectionEvent(ActionEvent event) {
        if (objectComboBox.getSelectionModel()!=null) {
            nameTextField.setText(objectComboBox.getSelectionModel().getSelectedItem().getName());
            heightTextField.setText(String.valueOf(objectComboBox.getSelectionModel().getSelectedItem().getHeight()));
        }
    }

        public void errorWindow(String errorMsg) {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setFullScreen(false);
        dialog.setResizable(false);
        dialog.setTitle("Add Object");
        dialog.setIconified(false);
        VBox dialogVbox = new VBox(40);
        Text text = new Text(errorMsg);


        Scene dialogScene = new Scene(dialogVbox, 250, 80);
        dialog.setScene(dialogScene);
        dialogVbox.setPadding(new Insets(20, 20, 20, 20));
        dialogVbox.getChildren().add(text);
        dialog.show();
        parentController.notify("Object creation error: " + errorMsg);
    }
}
