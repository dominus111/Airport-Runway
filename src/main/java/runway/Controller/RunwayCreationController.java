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
    private TextField leftLDA,leftTODA,leftASDA,leftTORA,leftDispThr, rightDispThr, rightLDA, rightTODA, rightASDA, rightTORA;

    private String leftLdaStr = "", leftTodaStr = "", leftAsdaStr = "", leftToraStr = "", rightToraStr = "", rightTodaStr = "", rightLdaStr = "", rightAsdaStr = "";

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
        leftLDA.clear();
        leftTODA.clear();
        rightTODA.clear();
        rightTORA.clear();
        rightASDA.clear();
        rightLDA.clear();
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
        if(topHDirComboBox.getSelectionModel().getSelectedItem() == null || topRDirComboBox.getSelectionModel().getSelectedItem() == null || leftDispThr.getText() == null || rightDispThr == null || leftLDA.getText() == null || leftTODA.getText() == null || leftASDA == null || leftTORA == null || rightLDA == null || rightTORA.getText() == null || rightASDA.getText() == null || rightTORA.getText() == null) {
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
                double topLDA = Double.parseDouble(leftLDA.getText());
                double topTODA = Double.parseDouble(leftTODA.getText());
                double topASDA = Double.parseDouble(leftASDA.getText());
                double topTORA = Double.parseDouble(leftTORA.getText());
                double topDistThr = Double.parseDouble(leftDispThr.getText());

                double bottomLDA = Double.parseDouble(rightLDA.getText());
                double bottomTODA = Double.parseDouble(rightTODA.getText());
                double bottomASDA = Double.parseDouble(rightASDA.getText());
                double bottomTORA = Double.parseDouble(rightTORA.getText());
                double bottomDistThr = Double.parseDouble(rightDispThr.getText());

                if (topLDA < 500 || bottomLDA < 500)
                    errorWindow("LDA value must be between 500 and 5000 inclusive.");
                else if (topASDA < 1000 || bottomASDA < 1000)
                    errorWindow("ASDA value must be between 1000 and 10000 inclusive.");
                else if (topTORA < 1000 || bottomTORA < 1000)
                    errorWindow("TORA value must be between 1000 and 5000 inclusive.");
                else if (topTODA < 1000 || bottomTODA < 1000)
                    errorWindow("TODA value must be between 1000 and 10000 inclusive.");
                else if (bottomLDA + topLDA - 500 < topTORA || bottomLDA + topLDA - 500 < bottomTORA )
                    errorWindow("LDA values must sum to a value that is at least 500 greater than TORA.");
                else {
                    if(topHValue > 18) {
                        if (bottomTODA < bottomTORA) {
                            errorWindow("Lower Threshold TODA value must be greater than or equal to TORA value.");
                            error = true;
                        }
                        if (topASDA < topTORA) {
                            errorWindow( "Higher Threshold ASDA value must be greater than or equal to TORA value.");
                            error = true;
                        }
                    } else {
                        if (topTODA < topTORA) {
                            errorWindow("Lower Threshold TODA value must be greater than or equal to TORA value.");
                            error = true;
                        }
                        if (bottomASDA < bottomTORA) {
                            errorWindow( "Higher Threshold ASDA value must be greater than or equal to TORA value.");
                            error = true;
                        }
                    }

                    if(!error) {
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
              parentController.notify("Runway creation error: only doubles allowed in the text fields.");
            }

        }
    }

    @FXML
    void leftLdaKeyTyped (KeyEvent event) {
        leftLdaStr = parentController.checkDouble(leftLDA, leftLdaStr, 0, 5000, 12);
    }

    @FXML
    void rightLdaKeyTyped (KeyEvent event) {
        rightLdaStr = parentController.checkDouble(rightLDA, rightLdaStr, 0, 5000, 12);
    }
    @FXML
    void leftToraKeyTyped (KeyEvent event) {
        leftToraStr = parentController.checkDouble(leftTORA, leftToraStr, 0, 5000, 12);
    }
    @FXML
    void rightToraKeyTyped (KeyEvent event) {
        rightToraStr = parentController.checkDouble(rightTORA, rightToraStr, 0, 5000, 12);
    }

    @FXML
    void leftTodaKeyTyped (KeyEvent event) {
        leftTodaStr = parentController.checkDouble(leftTODA, leftTodaStr, 0, 10000, 12);
    }

    @FXML
    void rightTodaKeyTyped (KeyEvent event) {
        rightTodaStr = parentController.checkDouble(rightTODA, rightTodaStr, 0, 10000, 12);
    }
    @FXML
    void leftAsdaKeyTyped (KeyEvent event) {
        leftAsdaStr = parentController.checkDouble(leftASDA, leftAsdaStr, 0, 10000, 12);
    }
    @FXML
    void rightAsdaKeyTyped (KeyEvent event) {
        rightAsdaStr = parentController.checkDouble(rightASDA, rightAsdaStr, 0, 10000, 12);
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
