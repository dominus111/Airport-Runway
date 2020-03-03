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


public class RunwayCreationController {
    @FXML
    private Button cancelButton;
    @FXML
    private TextField leftLDA,leftTODA,leftASDA,leftTORA, rightLDA, rightTODA, rightASDA, rightTORA;
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

        leftASDA.redo();
    }

    @FXML
    void runwayCancelButtonEvent(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void createRunwayButtonEvent(ActionEvent event) {
        if(topHDirComboBox.getSelectionModel().getSelectedItem() == null || topRDirComboBox.getSelectionModel().getSelectedItem() == null || leftLDA.getText() == null || leftTODA.getText() == null || leftASDA == null || leftTORA == null || rightLDA == null || rightTORA.getText() == null || rightASDA.getText() == null || rightTORA.getText() == null)
           System.out.println("Error message. Empty fields");
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
            double topHValue = Double.parseDouble(topHDirComboBox.getSelectionModel().getSelectedItem());
            double topLDA = Double.parseDouble(leftLDA.getText());
            double topTODA = Double.parseDouble(leftTODA.getText());
            double topASDA = Double.parseDouble(leftASDA.getText());
            double topTORA = Double.parseDouble(leftTORA.getText());

            double bottomLDA = Double.parseDouble(rightLDA.getText());
            double bottomTODA = Double.parseDouble(rightTODA.getText());
            double bottomASDA = Double.parseDouble(rightASDA.getText());
            double bottomTORA = Double.parseDouble(rightTORA.getText());

            Airport airport = parentController.getAirport();
            if (topHValue > 18)
                airport.addRunway(new Runway(new VirtualRunway(topHValue - 18 + bottomRValue, new RunwayParameters(bottomTORA, bottomTODA, bottomASDA, bottomLDA)), new VirtualRunway(topHValue + topRValue, new RunwayParameters(topTORA, topTODA, topASDA, topLDA))));
            else
                airport.addRunway(new Runway(new VirtualRunway(topHValue + topRValue, new RunwayParameters(topTORA, topTODA, topASDA, topLDA)), new VirtualRunway(topHValue + 18 + bottomRValue, new RunwayParameters(bottomTORA, bottomTODA, bottomASDA, bottomLDA))));

            ComboBox<String> runwaySelect = parentController.getRunwaySelect();
            if(runwaySelect != null) {
                runwaySelect.getItems().clear();
                for(Runway currentRunway : airport.getObservableRunwayList()) {
                    runwaySelect.getItems().add(currentRunway.toString());
                }
            }
            this.initialize();
        }
    }
}
