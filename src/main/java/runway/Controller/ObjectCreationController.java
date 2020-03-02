package runway.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ObjectCreationController {
    @FXML
    private Button cancelButton;
    private Controller parentController;

    @FXML
    void objectCancelButtonEvent(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void setParentController(Controller parentController) {
        this.parentController = parentController;
    }
}
