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
import runway.Model.*;
import javafx.scene.input.KeyEvent;

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

    String height = "";
    String distToCL = "";
    String topThr = "";
    String bottomThr = "";
    Runway current;

    @FXML
    void objectCancelButtonEvent(ActionEvent event) {
        parentController.makeGraphicsVisible(true);
        parentController.disableViewButtons(false);
        parentController.setAllButtonsDisable(false);
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void setRunway(Runway run) {
        this.current = run;
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
                errorWindow("Name field can contain between 1 and 20 characters. \n" +
                        "The object's height must be a positive value smaller or equal to 50. \n" +
                        "Distance to center line value must be between -75 and 75. \n" +
                        "Distance to thresholds values must between -60 an 500.");
                disable = true;
            }
        }

        if (!disable) {
            parentController.setAllButtonsDisable(false);
            parentController.addObjButton.setDisable(true);
            if (parentController.leftB.isSelected()) {
                parentController.topLeftButton(new ActionEvent());
            } else
                parentController.topRightButton(new ActionEvent());

        }
     }

    @FXML
    void objectSelectionEvent(ActionEvent event) {
        if (objectComboBox.getSelectionModel()!=null) {
            nameTextField.setText(objectComboBox.getSelectionModel().getSelectedItem().getName());
            heightTextField.setText(String.valueOf(objectComboBox.getSelectionModel().getSelectedItem().getHeight()));
            height = String.valueOf(objectComboBox.getSelectionModel().getSelectedItem().getHeight());
        }
    }

    @FXML
    void nameKeyTyped ( KeyEvent event) {
        String string = nameTextField.getText();
        if(string.length() > 20 ) {
            nameTextField.setText(string.substring(0, string.length()-1));
            nameTextField.positionCaret(string.length()-1);
        }
    }

    @FXML
    void heightOnKeyTyped (KeyEvent event) {
        height = parentController.checkDouble(heightTextField,height, 1, 50, 12);
    }

    @FXML
    void dCentrLOnKeyTyped (KeyEvent event) {
        VirtualRunway v = current.getLeftRunway();
        int val = (int) v.getInitialParameters().getTora() / 15;
        distToCL = parentController.checkDouble(distToCLTextField, distToCL, -val, val, 12);
    }

    @FXML
    void displThrTopKeyTyped (KeyEvent event) {
        VirtualRunway v = current.getLeftRunway();
        int val = (int) v.getInitialParameters().getTora();
                topThr = parentController.checkDouble(distToThrTextField, topThr, 0, val, 12);
    }

    @FXML
    void displThrBottomKeyTyped (KeyEvent event) {
        VirtualRunway v = current.getRightRunway();
        int val = (int) v.getInitialParameters().getTora();
        bottomThr = parentController.checkDouble(bottomDistToThrTextF, bottomThr, 0, val, 12);
    }


    public void errorWindow(String errorMsg) {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setFullScreen(false);
        dialog.setTitle("Add Object");
        dialog.setIconified(false);
        VBox dialogVbox = new VBox(100);
        Text text = new Text(errorMsg);

        Scene dialogScene = new Scene(dialogVbox, 400, 100);
        dialog.setScene(dialogScene);
        dialogVbox.setPadding(new Insets(20, 20, 20, 20));
        dialogVbox.getChildren().add(text);
        dialog.show();
        parentController.notify("Object creation error: " + errorMsg);
    }
}
