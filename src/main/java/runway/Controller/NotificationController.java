package runway.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Background;

public class NotificationController {
    Controller parent;

    @FXML
    ScrollPane content;

    @FXML
    public void initialize() {

    }

    public void setParentController(Controller controller) {
        parent = controller;
    }
}
