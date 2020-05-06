package runway.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
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
        stage.close();
    }

    @FXML
    void createRunwayButtonEvent(ActionEvent event) {
        if(topHDirComboBox.getSelectionModel().getSelectedItem() == null || topRDirComboBox.getSelectionModel().getSelectedItem() == null || leftDispThr.getText() == null || rightDispThr == null || leftLDA.getText() == null || leftTODA.getText() == null || leftASDA == null || leftTORA == null || rightLDA == null || rightTORA.getText() == null || rightASDA.getText() == null || rightTORA.getText() == null) {
            parentController.setAllButtonsDisable(false);
            parentController.notify("Runway creation error: empty fields");
        }
        else {
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

            Airport airport = parentController.getAirport();
            Runway runway;
            if (topHValue > 18){
                runway = new Runway(new VirtualRunway(Math.round(topHValue)  - 18 + bottomRValue, new RunwayParameters(bottomTORA, bottomTODA, bottomASDA, bottomLDA,  bottomDistThr)), new VirtualRunway(Math.round(topHValue) + topRValue, new RunwayParameters(topTORA, topTODA, topASDA, topLDA, topDistThr)));
                airport.addRunway(runway);
            }
            else{
                    runway = new Runway(new VirtualRunway(Math.round(topHValue) + topRValue, new RunwayParameters(topTORA, topTODA, topASDA, topLDA, topDistThr)), new VirtualRunway(Math.round(topHValue) + 18 + bottomRValue, new RunwayParameters(bottomTORA, bottomTODA, bottomASDA, bottomLDA, bottomDistThr)));
                    airport.addRunway(runway);
            }
            ComboBox<String> runwaySelect = parentController.getRunwaySelect();
            if(runwaySelect != null) {
                runwaySelect.getItems().clear();
                for(Runway currentRunway : airport.getObservableRunwayList()) {
                    runwaySelect.getItems().add(currentRunway.toString());
                }
            }
            parentController.notify("Runway " + runway + " added to airport");
            parentController.setAllButtonsDisable(false);

            runwayCancelButtonEvent(event);
            }  catch (NullPointerException | NumberFormatException ex)
            {
              parentController.setAllButtonsDisable(false);
              parentController.notify("Runway creation error: only doubles allowed in the text fields.");
            }

        }
    }
}
