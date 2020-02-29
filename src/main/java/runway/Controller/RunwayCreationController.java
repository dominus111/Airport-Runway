package runway.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import runway.Model.Airport;
import javax.management.Notification;

public class RunwayCreationController {
    @FXML
    private Button cancelButton;
    @FXML
    private TextField leftHeading,leftLDA,leftTODA,leftASDA,leftTORA;

    private Airport airport;

    public void setAirport(Airport airport) {
        this.airport = airport;
    }

    @FXML
    void runwayCancelButtonEvent(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void createRunwayButtonEvent(ActionEvent event) {
        if(leftHeading.getText() == null || leftLDA.getText() == null || leftTODA.getText() == null || leftASDA == null || leftTORA == null )
            return;

    }
}
