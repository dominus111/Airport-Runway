package runway.Controller;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NotificationController {
    Controller parent;
    Stage stage;
    ArrayList<String> notifications = new ArrayList<>();
    int notificationSize = 20;

    @FXML
    AnchorPane content;

    @FXML
    public void initialize() {

    }

    public void setParentController(Controller controller) {
        parent = controller;
    }

    public void update() {
        content.getChildren().clear();
        int scaler = parent.getNotifications().size() - 1;
        content.setPrefHeight((scaler + 2)*notificationSize);
        for(String s : parent.getNotifications()){
            Label notification = new Label(s);
            notification.setBackground(new Background(new BackgroundFill(new Color(1,1,1,1.0), CornerRadii.EMPTY, Insets.EMPTY)));
            notification.setTranslateY(notificationSize*scaler);
            content.getChildren().add(notification);
            scaler--;
        }
    }
}
