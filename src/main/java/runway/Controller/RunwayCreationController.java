package runway.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import runway.Model.Airport;
import runway.Model.Runway;
import runway.Model.RunwayParameters;
import runway.Model.VirtualRunway;

import java.lang.reflect.InvocationTargetException;


public class RunwayCreationController {
    @FXML
    private Button cancelButton;
    @FXML
    private TextField leftTODA,leftASDA,leftTORA,leftDispThr, rightDispThr, rightTODA, rightASDA, rightTORA;

    private String leftThrStr = "", leftTodaStr = "", leftAsdaStr = "", leftToraStr = "", rightToraStr = "", rightTodaStr = "", rightThrStr = "", rightAsdaStr = "";

    @FXML
    private ComboBox<String> topRDirComboBox, topHDirComboBox;

    private Controller parentController;

    public void setParentController (Controller parentController) {
        this.parentController = parentController;
    }

    @FXML
    public void initialize() {
        if(topRDirComboBox != null) {
            topRDirComboBox.getItems().clear();
            topRDirComboBox.getItems().addAll("Left", "Right", "Center", "None");
        }

        if (topHDirComboBox != null) {
            topHDirComboBox.getItems().clear();
            for( int i = 1; i <= 36; i++)
            topHDirComboBox.getItems().add(String. valueOf(i));
        }

        leftASDA.clear();
        leftTORA.clear();
        leftTODA.clear();
        rightTODA.clear();
        rightTORA.clear();
        rightASDA.clear();
    }

    @FXML
    void runwayCancelButtonEvent(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();

        parentController.setAllButtonsDisable(false);
        parentController.runwaySelectEvent(event);
        stage.close();
    }

    @FXML
    void createRunwayButtonEvent(ActionEvent event) {
        boolean error = false;
        if(topHDirComboBox.getSelectionModel().getSelectedItem() == null || topRDirComboBox.getSelectionModel().getSelectedItem() == null || leftDispThr.getText() == null || rightDispThr == null || leftTODA.getText() == null || leftASDA == null || leftTORA == null || rightTORA.getText() == null || rightASDA.getText() == null || rightTORA.getText() == null) {
            errorWindow("Empty fields are not allowed.");
        } else {
            String topRValue = topRDirComboBox.getSelectionModel().getSelectedItem();
            String bottomRValue;
            switch (topRValue) {
                case "Right":
                    bottomRValue = "L";
                    break;
                case "Left":
                    bottomRValue = "R";
                    break;
                case "Center":
                    bottomRValue = "C";
                    break;
                default:
                    bottomRValue = "";
                    break;
            }

            topRValue = String.valueOf(topRValue.charAt(0));
            try {

                double topHValue = Double.parseDouble(topHDirComboBox.getSelectionModel().getSelectedItem());
                double topTODA = Double.parseDouble(leftTODA.getText());
                double topASDA = Double.parseDouble(leftASDA.getText());
                double topTORA = Double.parseDouble(leftTORA.getText());
                double topDistThr = Double.parseDouble(leftDispThr.getText());
                double topLDA = topTORA - topDistThr;

                double bottomTODA = Double.parseDouble(rightTODA.getText());
                double bottomASDA = Double.parseDouble(rightASDA.getText());
                double bottomTORA = Double.parseDouble(rightTORA.getText());
                double bottomDistThr = Double.parseDouble(rightDispThr.getText());
                double bottomLDA = bottomTORA - bottomDistThr;

                if( topLDA > topDistThr || bottomLDA > bottomDistThr ) {
                    errorWindow("The displaced Threshold value cannot be bigger than the TORA value");
                } else {
                    if (topHValue > 18) {
                        if (bottomTODA < bottomTORA) {
                            errorWindow("Lower Threshold TODA value must be greater than or equal to TORA value.");
                            error = true;
                        }
                        if (topASDA < topTORA) {
                            errorWindow("Higher Threshold ASDA value must be greater than or equal to TORA value.");
                            error = true;
                        }
                    } else {
                        if (topTODA < topTORA) {
                            errorWindow("Lower Threshold TODA value must be greater than or equal to TORA value.");
                            error = true;
                        }
                        if (bottomASDA < bottomTORA) {
                            errorWindow("Higher Threshold ASDA value must be greater than or equal to TORA value.");
                            error = true;
                        }
                    }

                    if (!error) {
                        Airport airport = parentController.getAirport();
                        Runway runway;
                        if (topHValue > 18) {
                            runway = new Runway(new VirtualRunway(Math.round(topHValue) - 18 + bottomRValue, new RunwayParameters(bottomTORA, bottomTODA, bottomASDA, bottomLDA, bottomDistThr)), new VirtualRunway(Math.round(topHValue) + topRValue, new RunwayParameters(topTORA, topTODA, topASDA, topLDA, topDistThr)));
                            airport.addRunway(runway);
                        } else {
                            runway = new Runway(new VirtualRunway(Math.round(topHValue) + topRValue, new RunwayParameters(topTORA, topTODA, topASDA, topLDA, topDistThr)), new VirtualRunway(Math.round(topHValue) + 18 + bottomRValue, new RunwayParameters(bottomTORA, bottomTODA, bottomASDA, bottomLDA, bottomDistThr)));
                            airport.addRunway(runway);
                        }
                        ComboBox<String> runwaySelect = parentController.getRunwaySelect();
                        if (runwaySelect != null) {
                            runwaySelect.getItems().clear();
                            for (Runway currentRunway : airport.getObservableRunwayList()) {
                                runwaySelect.getItems().add(currentRunway.toString());
                            }
                        }
                        parentController.notify("Runway " + runway + " added to airport");
                        parentController.setAllButtonsDisable(false);
                        runwayCancelButtonEvent(event);
                    }
                }
            }  catch (NullPointerException | NumberFormatException ex) {
              parentController.setAllButtonsDisable(false);
              runwayCancelButtonEvent(event);
              parentController.notify("Runway creation error: only doubles allowed in the text fields.");
            }
        }
    }

    @FXML
    void leftThrKeyTyped (KeyEvent event) {
        leftThrStr = parentController.checkDouble(leftDispThr, leftThrStr, 0, 20000, 12);
    }

    @FXML
    void rightThrKeyTyped (KeyEvent event) {
        rightThrStr = parentController.checkDouble(rightDispThr, rightThrStr, 0, 20000, 12);
    }

    @FXML
    void leftToraKeyTyped (KeyEvent event) {
        leftToraStr = parentController.checkDouble(leftTORA, leftToraStr, 0, 20000, 12);
    }
    @FXML
    void rightToraKeyTyped (KeyEvent event) {
        rightToraStr = parentController.checkDouble(rightTORA, rightToraStr, 0, 20000, 12);
    }

    @FXML
    void leftTodaKeyTyped (KeyEvent event) {
        leftTodaStr = parentController.checkDouble(leftTODA, leftTodaStr, 0, 20000, 12);
    }

    @FXML
    void rightTodaKeyTyped (KeyEvent event) {
        rightTodaStr = parentController.checkDouble(rightTODA, rightTodaStr, 0, 20000, 12);
    }
    @FXML
    void leftAsdaKeyTyped (KeyEvent event) {
        leftAsdaStr = parentController.checkDouble(leftASDA, leftAsdaStr, 0, 20000, 12);
    }
    @FXML
    void rightAsdaKeyTyped (KeyEvent event) {
        rightAsdaStr = parentController.checkDouble(rightASDA, rightAsdaStr, 0, 20000, 12);
    }

    public void errorWindow(String errorMsg) {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setFullScreen(false);
        dialog.setTitle("Add Object");
        dialog.setIconified(false);
        VBox dialogVbox = new VBox(100);
        Text text = new Text(errorMsg);

        Scene dialogScene = new Scene(dialogVbox, 400, 130);
        dialog.setScene(dialogScene);
        dialogVbox.setPadding(new Insets(20, 20, 20, 20));
        dialogVbox.getChildren().add(text);
        dialog.show();
        parentController.notify("Object creation error: " + errorMsg);
    }

}
