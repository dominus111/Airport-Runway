package runway.Controller;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.*;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.*;
import javafx.util.Callback;
import runway.Model.*;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class Controller {

    @FXML
    ComboBox<Airport> airportList;

    @FXML
    private ComboBox<String> runwaySelect;
    private Airport airport;
    private Runway current;

    private Color colour = Color.WHITE;
    private Boolean leftView = true;
    private Boolean topTakingoff = true;

    @FXML
    protected Button addObjButton;

    @FXML
    private AnchorPane notificationBox;
    @FXML
    private AnchorPane sideOnAnchorPane;
    @FXML
    private AnchorPane topView, topRunawayPane;
    @FXML
    private Line lineTODA, lineASDA, lineTORA, lineLDA, centerLine, leftStart, rightStart;
    @FXML
    private Text textTODA, textASDA, textTORA, textLDA;
    @FXML
    private Text designatorL, designatorR;
    @FXML
    private Polygon topLeftArrow, topRightArrow;
    @FXML
    private Rectangle topYellow, topRed, topOrange;
    @FXML
    private Circle obstacle;

    /**
     * SIDE VIEW
     */

    @FXML
    private Text sideTORA, sideLDA, sideTODA, sideASDA;
    @FXML
    private RadioButton sideLeftButton, sideRightButton;
    @FXML
    private RadioButton leftB, rightB;
    @FXML
    private Line sideLineTODA, sideLineLDA, sideLineTORA, sideLineASDA;
    @FXML
    private Rectangle rightStopway, rightClearway, sideDisplacedThreshold, topRunaway, sideObstacle;

    @FXML
    private TableView<RunwayParameters> topLtableView, topRtableView;
    @FXML
    private TableColumn<RunwayParameters, Double> leftTora, leftToda, leftAsda, leftLda, rightTora, rightToda, rightAsda, rightLda;
    @FXML
    private Label leftRParamLabel, rightRParamLabel;

    /**
     * NOTIFICATIONS
     */

    @FXML
    private Label notification1, notification2, notification3;

    private NotificationController notificationController;
    private int notificationCount = 0;
    private ArrayList<String> notifications = new ArrayList<>();
    private Boolean notificationsVisible = false;

    /**
     * XML
     */
    @FXML
    private Button xmlLoad, xmlImport, xmlExport;
    @FXML
    private ComboBox<String> xmlCombo;

    XMLImport xmlImporter = new XMLImport();
    XMLExport xmlExporter = new XMLExport();

    ObservableList<Airport> options;

    @FXML
    public void initialize() {
        //TODO fix double airports
        options = FXCollections.observableArrayList(xmlImporter.getAirports());
        if(airportList!=null) {
            airportList.setItems(options);
            System.out.println(options.size());
        }

        if(options.isEmpty()){
            airport = new Airport();
        }
        else{
            airport = options.get(0);
        }
        if (runwaySelect != null) {
            runwaySelect.getItems().clear();
            for (Runway currentRunway : airport.getObservableRunwayList()) {
                runwaySelect.getItems().add(currentRunway.toString());
            }
        }
    }


    public ComboBox<String> getRunwaySelect() {
        return runwaySelect;
    }

    public Airport getAirport() {
        return airport;
    }

    @FXML
    void notificationPopOutAction(){
        if(notificationsVisible){
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Notification.fxml"));
            Parent root = loader.load();
            notificationController = loader.getController();
            notificationController.setParentController(this);

            Stage stage = new Stage();
            stage.setTitle("Notifications");
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    notificationsVisible = false;
                }
            });
            stage.setScene(new Scene(root));
            stage.show();
            notificationsVisible = true;
            notificationController.update();
        } catch (Exception e) {
            e.printStackTrace();
        }
        updateTables();
    }

    ArrayList<String> getNotifications(){
        return notifications;
    }

    void notify(String message){
        String text = message;
        notifications.add(message);
        try {
            notificationController.update();
        }
        catch(Exception e){}

        notificationCount++;
        switch(notificationCount){
            case 1:
                notification1.setText(text);
                notification1.setTextFill(new Color(0,0,0,1));
                break;
            case 2:
                notification2.setText(notification1.getText());
                notification1.setText(text);
                notification2.setTextFill(new Color(0.35,0.35,0.35,1));
                notification3.setTextFill(new Color(0.65,0.65,0.65,1));
                break;
            default:
                notification3.setText("(" + (notificationCount - 2) + " more)");
                notification2.setText(notification1.getText());
                notification1.setText(text);
                break;
        }


    }

    @FXML
    void sideOnTabEvent(Event event) {
        if (runwaySelect.getValue() != null) {
            sideOnAnchorPane.setVisible(true);
            sideLeftButton.setDisable(false);
            sideRightButton.setDisable(false);
            runwayUpdate();
        }
    }


    public void leftSideButtonClick(ActionEvent actionEvent) {
        if (runwaySelect.getValue() != null) {
            leftView = true;
            runwayUpdate();
        }
    }

    public void rightSideButtonClick(ActionEvent actionEvent) {
        if (runwaySelect.getValue() != null) {
            leftView = false;
            runwayUpdate();
        }
    }

    public void topLeftButton(ActionEvent actionEvent) {
        if (runwaySelect.getValue() != null) {
            leftView = true;
            topRunwayUpdate();
        }
    }

    public void topRightButton(ActionEvent actionEvent) {
        if (runwaySelect.getValue() != null) {
            leftView = false;
            topRunwayUpdate();
        }
    }

    public void topTakingoffButton(ActionEvent actionEvent) {
        if (runwaySelect.getValue() != null) {
            topTakingoff = true;
            topRunwayUpdate();
        }
    }

    public void topLandingButton(ActionEvent actionEvent) {
        if (runwaySelect.getValue() != null) {
            topTakingoff = false;
            topRunwayUpdate();
        }
    }

    private void getInitialTopDown() {
        //centerLine.getStrokeDashArray().addAll(3.0,7.0,3.0,7.0);
        //leftStart.getStrokeDashArray().addAll(3.0,7.0,3.0,7.0);
        //rightStart.getStrokeDashArray().addAll(3.0,7.0,3.0,7.0);
    }

    public void topRunwayUpdate() {
        int LTODA = 16;
        int LASDA = 49;
        int LTORA = 119;
        int RTODA = 626;
        int RASDA = 599;
        int RTORA = 544;
        int TTODA = RTODA - LTORA;
        int TTORA = RTORA - LTORA;
        int LCOLOR = LTORA;
        int RCOLOR = RTORA;
        int COLOR_DIST = TTODA;
        int COLOR_OFFSET = 4;
        int TORA_ASDA_DIST = LTORA - LASDA;
        double lineLenght = 55 / 2;
        double viewOffset = 12;
        int obstRadius = 16;
        int stripEnd = 60;
        int slope = 50;
        int RESA = 240;
        int BLAST = 300;

        /**
         * Create an obstacle and its parameters and put it in the choice box
         */
        //Obstacle plane1 = new Obstacle("Plane1", 10.0);
        //ObstaclePositionParam plane1Parm = new ObstaclePositionParam(plane1, 500, 500, 0);
        //plane1.setoParam(plane1Parm);

        int obstHight = 0;
        int obstLTHR = 0;
        int obstRTHR = 0;
        int obstCLine = 0;
        double obstDisp = 0;
        double obstOffDisp = 0;
        boolean boolObstacle = false;


        if( current.getObstacle() != null){
            obstacle.setVisible(true);
            obstHight = (int) current.getObstacle().getHeight();
            obstLTHR = (int) current.getObstacle().getoParam().getDistToLTHR();
            obstRTHR = (int) current.getObstacle().getoParam().getDistToRTHR();
            obstCLine = (int) current.getObstacle().getoParam().getDistToCenterL();
            obstDisp = obstHight * slope + RESA + stripEnd;
            obstOffDisp = obstLTHR - (RESA + stripEnd);
            boolObstacle = true;
        }
        else{
            obstacle.setVisible(false);
            boolObstacle = false;
        }

        /**
         *
         * RUNWAY
         */
        VirtualRunway v;
        leftB.setText(current.getLeftRunway().toString());
        rightB.setText(current.getRightRunway().toString());

        if (runwaySelect.getValue() == null) {
            topRed.setVisible(false);
            topYellow.setVisible(false);
            topOrange.setVisible(false);
        }

        lineTODA.setStroke(colour);
        lineASDA.setStroke(colour);
        lineTORA.setStroke(colour);
        lineLDA.setStroke(colour);


        designatorL.setText(current.getLeftRunway().getDesignator());
        designatorR.setText(current.getRightRunway().getDesignator());

        centerLine.getStrokeDashArray().addAll(7.0, 14.0, 7.0, 14.0);
        leftStart.getStrokeDashArray().addAll(3.0, 7.0, 3.0, 7.0);
        rightStart.getStrokeDashArray().addAll(3.0, 7.0, 3.0, 7.0);

        topRunaway.setLayoutX(45);
        topRunaway.setWidth(550);

        centerLine.setLayoutX(121);
        centerLine.setEndX(404);

        leftStart.setLayoutX(92);
        rightStart.setLayoutX(495);

        if (leftView) {
            v = current.getLeftRunway();
            topRightArrow.setVisible(true);
            topLeftArrow.setVisible(false);

            topRed.setVisible(true);
            topYellow.setVisible(true);
            topOrange.setVisible(true);

            double tora = v.getInitialParameters().getTora();
            double lda = v.getInitialParameters().getLda();
            double toda = v.getInitialParameters().getToda();
            double asda = v.getInitialParameters().getAsda();

            if (boolObstacle) {
                double obstoffset = (TTODA * obstLTHR / toda);
                double obstDisplacement = (TTODA * obstDisp / toda);
                double obstFromCLine = (TTODA * obstCLine / toda);
                double blast = (TTODA * BLAST / toda);
                obstacle.setLayoutY(centerLine.getLayoutY() + obstFromCLine);

                if (tora / 2 > obstLTHR) {
                    if (topTakingoff) {
                        obstacle.setLayoutX(centerLine.getLayoutX() + obstoffset);

                        double ldaoffset2 = (TTODA * tora / toda - (blast + obstoffset));
                        double asdaoffset = (TTODA * asda / toda);
                        double toraoffset = (TTODA * tora / toda);

                        double yellowoffset = toraoffset - ldaoffset2;
                        double redoffset = toraoffset + LTORA;
                        double orangeoffset = toraoffset + LTORA;

                        double ldaoffset = centerLine.getLayoutX() + obstoffset + blast;

                        topRunaway.setWidth(asdaoffset + TORA_ASDA_DIST + 4);
                        centerLine.setEndX(toraoffset);
                        rightStart.setLayoutX(toraoffset + LTORA - lineLenght - 2);

                        topYellow.setLayoutX(LCOLOR);
                        topYellow.setWidth(yellowoffset);

                        topRed.setLayoutX(redoffset);
                        topRed.setWidth(RTODA - redoffset);

                        topOrange.setLayoutX(orangeoffset);
                        topOrange.setWidth(asdaoffset - toraoffset);

                        lineTODA.setLayoutX(LTORA);
                        lineASDA.setLayoutX(LTORA);
                        lineTORA.setLayoutX(LTORA);
                        lineLDA.setLayoutX(ldaoffset);

                        lineTODA.setEndX(RTODA - LTORA);
                        lineASDA.setEndX(asdaoffset);
                        lineTORA.setEndX(toraoffset);
                        lineLDA.setEndX(ldaoffset2);

                        textTORA.setText("TORA: " + Math.round(tora) + "m");
                        textLDA.setText("LDA: " + Math.round(tora - obstLTHR - BLAST) + "m");
                        textTODA.setText("TODA: " + Math.round(toda) + "m");
                        textASDA.setText("ASDA: " + Math.round(asda) + "m");

                        /**
                         *  LANDING
                          */
                    } else {
                        obstacle.setLayoutX(centerLine.getLayoutX() + obstoffset);

                        double ldaoffset2 = (TTODA * tora / toda - (obstDisplacement + obstoffset));
                        double asdaoffset = (TTODA * asda / toda);
                        double toraoffset = (TTODA * tora / toda);

                        double yellowoffset = toraoffset - ldaoffset2;
                        double redoffset = toraoffset + LTORA;
                        double orangeoffset = toraoffset + LTORA;

                        double ldaoffset = centerLine.getLayoutX() + obstoffset + obstDisplacement;

                        topRunaway.setWidth(asdaoffset + TORA_ASDA_DIST + 4);
                        centerLine.setEndX(toraoffset);
                        rightStart.setLayoutX(toraoffset + LTORA - lineLenght - 2);

                        topYellow.setLayoutX(LCOLOR);
                        topYellow.setWidth(yellowoffset);

                        topRed.setLayoutX(redoffset);
                        topRed.setWidth(RTODA - redoffset);

                        topOrange.setLayoutX(orangeoffset);
                        topOrange.setWidth(asdaoffset - toraoffset);

                        lineTODA.setLayoutX(LTORA);
                        lineASDA.setLayoutX(LTORA);
                        lineTORA.setLayoutX(LTORA);
                        lineLDA.setLayoutX(ldaoffset);

                        lineTODA.setEndX(RTODA - LTORA);
                        lineASDA.setEndX(asdaoffset);
                        lineTORA.setEndX(toraoffset);
                        lineLDA.setEndX(ldaoffset2);

                        textTORA.setText("TORA: " + Math.round(tora) + "m");
                        textLDA.setText("LDA: " + Math.round(tora - obstLTHR - obstDisp) + "m");
                        textTODA.setText("TODA: " + Math.round(toda) + "m");
                        textASDA.setText("ASDA: " + Math.round(asda) + "m");
                    }
                    /**
                     *
                     * OBSTACLE THE RIGHT END
                     */
                } else {
                    double obstStart = centerLine.getLayoutX() + obstoffset;
                    obstacle.setLayoutX(obstStart);

                    double asdaoffset = (TTODA * asda / toda);
                    double toraoffset = (TTODA * tora / toda);
                    double ldaoffset2 = (TTODA * obstOffDisp / toda);

                    double yellowoffset = toraoffset - ldaoffset2;
                    double redoffset = toraoffset + LTORA;
                    double orangeoffset = toraoffset + LTORA;

                    double ldaoffset = LTORA;

                    topRunaway.setWidth(asdaoffset + TORA_ASDA_DIST + 4);
                    centerLine.setEndX(toraoffset);
                    rightStart.setLayoutX(toraoffset + LTORA - lineLenght - 2);

                    topYellow.setLayoutX(ldaoffset2 + LTORA);
                    topYellow.setWidth(yellowoffset);

                    topRed.setLayoutX(redoffset);
                    topRed.setWidth(RTODA - redoffset);

                    topOrange.setLayoutX(orangeoffset);
                    topOrange.setWidth(asdaoffset - toraoffset);

                    lineTODA.setLayoutX(LTORA);
                    lineASDA.setLayoutX(LTORA);
                    lineTORA.setLayoutX(LTORA);
                    lineLDA.setLayoutX(ldaoffset);

                    lineTODA.setEndX(RTODA - LTORA);
                    lineASDA.setEndX(asdaoffset);
                    lineTORA.setEndX(toraoffset);
                    lineLDA.setEndX(ldaoffset2);

                    textTORA.setText("TORA: " + Math.round(tora) + "m");
                    textLDA.setText("LDA: " + Math.round(obstOffDisp) + "m");
                    textTODA.setText("TODA: " + Math.round(toda) + "m");
                    textASDA.setText("ASDA: " + Math.round(asda) + "m");

                }
                /**
                 *
                 * NO OBSTACLE
                 */
            } else {

                double ldaoffset2 = (TTODA * lda / toda);
                double asdaoffset = (TTODA * asda / toda);
                double toraoffset = (TTODA * tora / toda);

                double yellowoffset = toraoffset - ldaoffset2;
                double redoffset = toraoffset + LTORA;
                double orangeoffset = toraoffset + LTORA;

                double ldaoffset = LTORA + (TTODA - (TTODA * lda / toda) - (RTODA - redoffset));

                topRunaway.setWidth(asdaoffset + TORA_ASDA_DIST + 4);
                centerLine.setEndX(toraoffset);
                rightStart.setLayoutX(toraoffset + LTORA - lineLenght - 2);

                topYellow.setLayoutX(LCOLOR);
                topYellow.setWidth(yellowoffset);

                topRed.setLayoutX(redoffset);
                topRed.setWidth(RTODA - redoffset);

                topOrange.setLayoutX(orangeoffset);
                topOrange.setWidth(asdaoffset - toraoffset);

                lineTODA.setLayoutX(LTORA);
                lineASDA.setLayoutX(LTORA);
                lineTORA.setLayoutX(LTORA);
                lineLDA.setLayoutX(ldaoffset);

                lineTODA.setEndX(RTODA - LTORA);
                lineASDA.setEndX(asdaoffset);
                lineTORA.setEndX(toraoffset);
                lineLDA.setEndX(ldaoffset2);

                textTORA.setText("TORA: " + Math.round(tora) + "m");
                textLDA.setText("LDA: " + Math.round(tora) + "m");
                textTODA.setText("TODA: " + Math.round(toda) + "m");
                textASDA.setText("ASDA: " + Math.round(asda) + "m");
            }
            /**
             *
             * RIGHT RUNWAY
             */
        } else {
            v = current.getRightRunway();
            topLeftArrow.setVisible(true);
            topRightArrow.setVisible(false);

            topRed.setVisible(true);
            topYellow.setVisible(true);
            topOrange.setVisible(true);

            double tora = v.getInitialParameters().getTora();
            double lda = v.getInitialParameters().getLda();
            double toda = v.getInitialParameters().getToda();
            double asda = v.getInitialParameters().getAsda();


            if (boolObstacle) {
                double obstoffset = (TTODA * (tora-obstRTHR) / toda);
                double obstDisplacement = (TTODA * obstDisp / toda);
                double obstFromCLine = (TTODA * obstCLine / toda);
                double blast = (TTODA * BLAST / toda);
                obstacle.setLayoutY(centerLine.getLayoutY() + obstFromCLine);

                if (tora / 2 < obstRTHR) {

                        double asdadist = (TTODA * asda / toda);
                        double toradist = (TTODA * tora / toda);
                        double ldadist = (TTODA * (obstRTHR - BLAST) / toda);

                        double asdaoffset = TTODA - asdadist + LTODA;
                        double toraoffset = TTODA - toradist + LTODA;
                        double ldaoffset = TTODA - toradist + LTODA;

                        double yellowoffset = toradist - ldadist;
                        double redoffset = TTODA - toradist;
                        double orangeoffset = asdadist - toradist;

                        obstacle.setLayoutX(toraoffset + obstoffset);

                        topRunaway.setLayoutX(asdaoffset - 2);
                        topRunaway.setWidth(asdadist + TORA_ASDA_DIST);

                        centerLine.setLayoutX(toraoffset);
                        centerLine.setEndX(toradist);

                        leftStart.setLayoutX(toraoffset - lineLenght);

                        topYellow.setLayoutX(toraoffset);
                        topYellow.setWidth(yellowoffset);

                        topRed.setLayoutX(LTODA);
                        topRed.setWidth(redoffset);

                        topOrange.setLayoutX(asdaoffset);
                        topOrange.setWidth(orangeoffset);

                        lineTODA.setLayoutX(LTODA);
                        lineASDA.setLayoutX(asdaoffset);
                        lineTORA.setLayoutX(toraoffset);
                        lineLDA.setLayoutX(toraoffset + obstoffset + blast);

                        lineTODA.setEndX(TTODA);
                        lineASDA.setEndX(asdadist);
                        lineTORA.setEndX(toradist);
                        lineLDA.setEndX(ldadist);

                        textTORA.setText("TORA: " + Math.round(tora) + "m");
                        textLDA.setText("LDA: " + Math.round(obstRTHR - BLAST) + "m");
                        textTODA.setText("TODA: " + Math.round(toda) + "m");
                        textASDA.setText("ASDA: " + Math.round(asda) + "m");

                    /**
                     *
                     * OBSTACLE CLOSER TO THE RIGHT SIDE
                     */
                } else {
                    if (topTakingoff) {
                        double asdadist = (TTODA * asda / toda);
                        double toradist = (TTODA * tora / toda);
                        double ldadist = (TTODA * (tora - (obstRTHR + RESA + stripEnd)) / toda);

                        double asdaoffset = TTODA - asdadist + LTODA;
                        double toraoffset = TTODA - toradist + LTODA;
                        double ldaoffset = TTODA - toradist + LTODA;

                        double yellowoffset = toradist - ldadist;
                        double redoffset = TTODA - toradist;
                        double orangeoffset = asdadist - toradist;

                        obstacle.setLayoutX(toraoffset + obstoffset);

                        topRunaway.setLayoutX(asdaoffset - 2);
                        topRunaway.setWidth(asdadist + TORA_ASDA_DIST);

                        centerLine.setLayoutX(toraoffset);
                        centerLine.setEndX(toradist);

                        leftStart.setLayoutX(toraoffset - lineLenght);

                        topYellow.setLayoutX(toraoffset + ldadist);
                        topYellow.setWidth(yellowoffset);

                        topRed.setLayoutX(LTODA);
                        topRed.setWidth(redoffset);

                        topOrange.setLayoutX(asdaoffset);
                        topOrange.setWidth(orangeoffset);

                        lineTODA.setLayoutX(LTODA);
                        lineASDA.setLayoutX(asdaoffset);
                        lineTORA.setLayoutX(toraoffset);
                        lineLDA.setLayoutX(toraoffset);

                        lineTODA.setEndX(TTODA);
                        lineASDA.setEndX(asdadist);
                        lineTORA.setEndX(toradist);
                        lineLDA.setEndX(ldadist);

                        textTORA.setText("TORA: " + Math.round(tora) + "m");
                        textLDA.setText("LDA: " + Math.round(tora - (obstRTHR + RESA + stripEnd)) + "m");
                        textTODA.setText("TODA: " + Math.round(toda) + "m");
                        textASDA.setText("ASDA: " + Math.round(asda) + "m");
                    } else {
                        double asdadist = (TTODA * asda / toda);
                        double toradist = (TTODA * tora / toda);
                        double ldadist = (TTODA * (tora - (obstRTHR + obstDisp)) / toda);

                        double asdaoffset = TTODA - asdadist + LTODA;
                        double toraoffset = TTODA - toradist + LTODA;
                        double ldaoffset = TTODA - toradist + LTODA;

                        double yellowoffset = toradist - ldadist;
                        double redoffset = TTODA - toradist;
                        double orangeoffset = asdadist - toradist;

                        obstacle.setLayoutX(toraoffset + obstoffset);

                        topRunaway.setLayoutX(asdaoffset - 2);
                        topRunaway.setWidth(asdadist + TORA_ASDA_DIST);

                        centerLine.setLayoutX(toraoffset);
                        centerLine.setEndX(toradist);

                        leftStart.setLayoutX(toraoffset - lineLenght);

                        topYellow.setLayoutX(toraoffset + ldadist);
                        topYellow.setWidth(yellowoffset);

                        topRed.setLayoutX(LTODA);
                        topRed.setWidth(redoffset);

                        topOrange.setLayoutX(asdaoffset);
                        topOrange.setWidth(orangeoffset);

                        lineTODA.setLayoutX(LTODA);
                        lineASDA.setLayoutX(asdaoffset);
                        lineTORA.setLayoutX(toraoffset);
                        lineLDA.setLayoutX(toraoffset);

                        lineTODA.setEndX(TTODA);
                        lineASDA.setEndX(asdadist);
                        lineTORA.setEndX(toradist);
                        lineLDA.setEndX(ldadist);

                        textTORA.setText("TORA: " + Math.round(tora) + "m");
                        textLDA.setText("LDA: " + Math.round(tora - (obstRTHR + obstDisp)) + "m");
                        textTODA.setText("TODA: " + Math.round(toda) + "m");
                        textASDA.setText("ASDA: " + Math.round(asda) + "m");
                    }
                }

            } else {

                double asdadist = (TTODA * asda / toda);
                double toradist = (TTODA * tora / toda);
                double ldadist = (TTODA * lda / toda);

                double asdaoffset = TTODA - asdadist + LTODA;
                double toraoffset = TTODA - toradist + LTODA;
                double ldaoffset = TTODA - toradist + LTODA;

                double yellowoffset = toradist - ldadist;
                double redoffset = TTODA - toradist;
                double orangeoffset = asdadist - toradist;

                topRunaway.setLayoutX(asdaoffset - 2);
                topRunaway.setWidth(asdadist + TORA_ASDA_DIST);

                centerLine.setLayoutX(toraoffset);
                centerLine.setEndX(toradist);

                leftStart.setLayoutX(toraoffset - lineLenght);

                topYellow.setLayoutX(ldaoffset + ldadist);
                topYellow.setWidth(yellowoffset);

                topRed.setLayoutX(LTODA);
                topRed.setWidth(redoffset);

                topOrange.setLayoutX(asdaoffset);
                topOrange.setWidth(orangeoffset);

                lineTODA.setLayoutX(LTODA);
                lineASDA.setLayoutX(asdaoffset);
                lineTORA.setLayoutX(toraoffset);
                lineLDA.setLayoutX(ldaoffset);

                lineTODA.setEndX(TTODA);
                lineASDA.setEndX(asdadist);
                lineTORA.setEndX(toradist);
                lineLDA.setEndX(ldadist);

                textTORA.setText("TORA: " + Math.round(tora) + "m");
                textLDA.setText("LDA: " + Math.round(lda) + "m");
                textTODA.setText("TODA: " + Math.round(toda) + "m");
                textASDA.setText("ASDA: " + Math.round(asda) + "m");
            }
        }

    }


    public void runwayUpdate() {
        VirtualRunway v;
        boolean updated;
        sideLeftButton.setText(current.getLeftRunway().toString());
        sideRightButton.setText(current.getRightRunway().toString());

        if (leftView) {
            v = current.getLeftRunway();
            updated = (current.getLeftRunway().getRecalculatedParameters() != null);
        } else {
            v = current.getRightRunway();
            updated = (current.getRightRunway().getRecalculatedParameters() != null);
        }
        Integer PIXEL_START = -302;
        Integer PIXEL_TOTAL = 632 - 100;
        Integer PIXEL_END = 316;

        double tora, lda, toda, asda, displacedThreshold;
        if(updated) {
            tora = v.getRecalculatedParameters().getTora();
            lda = v.getRecalculatedParameters().getLda();
            toda = v.getRecalculatedParameters().getToda();
            asda = v.getRecalculatedParameters().getAsda();
            displacedThreshold = v.getRecalculatedParameters().getdispTHR();
        }
        else{
            tora = v.getInitialParameters().getTora();
            lda = v.getInitialParameters().getLda();
            toda = v.getInitialParameters().getToda();
            asda = v.getInitialParameters().getAsda();
            displacedThreshold = v.getInitialParameters().getdispTHR();
        }

        double max = scale(tora, toda, lda, asda);

        double translate = 0;

        if( current.getObstacle() != null){
            sideObstacle.setVisible(true);
            if(leftView) {
                translate = PIXEL_TOTAL * (current.getObstacle().getoParam().getDistToLTHR() / max);
            }
            else{
                translate = PIXEL_TOTAL * (current.getObstacle().getoParam().getDistToRTHR() / max);
            }
            sideObstacle.setTranslateX(PIXEL_START + 317 + translate);
            translate += sideObstacle.getWidth();
            sideObstacle.setTranslateY(10 - current.getObstacle().getHeight());
            sideObstacle.setHeight(current.getObstacle().getHeight());
        }
        else{
            sideObstacle.setVisible(false);
        }


        sideTORA.setText("TORA: " + Math.round(tora) + "m");
        sideLineTORA.setStartX(PIXEL_START);
        sideLineTORA.setEndX(PIXEL_START + PIXEL_TOTAL * (tora / max));

        rightClearway.setTranslateX(PIXEL_START + 317 + PIXEL_TOTAL * (tora / max) + translate);

        rightStopway.setTranslateX(PIXEL_START + 317 + PIXEL_TOTAL * (tora / max) + translate);

        sideLDA.setText("LDA: " + Math.round(lda) + "m");
        sideLineLDA.setStartX(PIXEL_START + Double.max(PIXEL_TOTAL * (displacedThreshold / max), translate));
        sideLineLDA.setEndX(PIXEL_START + PIXEL_TOTAL * (lda / max) + Double.max(PIXEL_TOTAL * ((displacedThreshold) / max), translate));

        //any setTranslateX using PIXEL_START must take into account the actual pixel start of -317
        sideDisplacedThreshold.setTranslateX( PIXEL_START + 317 );
        sideDisplacedThreshold.setWidth(PIXEL_TOTAL * (displacedThreshold / max));

        sideTODA.setText("TODA: " + Math.round(toda) + "m");
        sideLineTODA.setStartX(PIXEL_START);
        sideLineTODA.setEndX(PIXEL_START + PIXEL_TOTAL * (toda / max));

        rightClearway.setWidth(PIXEL_TOTAL * ((toda - tora) / max) - translate);

        sideASDA.setText("ASDA: " + Math.round(asda) + "m");
        sideLineASDA.setStartX(PIXEL_START);
        sideLineASDA.setEndX(PIXEL_START + PIXEL_TOTAL * (asda / max));

        rightStopway.setWidth(PIXEL_TOTAL * ((asda - tora) / max) - translate);

        if(sideLineLDA.getStartX() < PIXEL_START){
            sideLineLDA.setStartX(PIXEL_START);
        }
        if(sideLineASDA.getStartX() < PIXEL_START){
            sideLineASDA.setStartX(PIXEL_START);
        }
        if(sideLineTODA.getStartX() < PIXEL_START){
            sideLineTODA.setStartX(PIXEL_START);
        }
        if(sideLineTORA.getStartX() < PIXEL_START){
            sideLineTORA.setStartX(PIXEL_START);
        }
        if(sideLineTORA.getEndX() > PIXEL_END){
            sideLineTORA.setEndX(PIXEL_END);
        }
        if(sideLineTODA.getEndX() > PIXEL_END){
            sideLineTODA.setEndX(PIXEL_END);
        }
        if(sideLineLDA.getEndX() > PIXEL_END){
            sideLineLDA.setEndX(PIXEL_END);
        }
        if(sideLineASDA.getEndX() > PIXEL_END){
            sideLineASDA.setEndX(PIXEL_END);
        }



    }

    public double scale(double tora, double toda, double lda, double asda) {
        double[] scalers = {tora, toda, lda, asda};
        double max = 0;
        for (double d : scalers) {
            if (d > max) {
                max = d;
            }
        }
        return max;
    }

    @FXML
    void topDownTabEvent(Event event) {
        if (runwaySelect != null)
            if (runwaySelect.getValue() != null) {
                topView.setVisible(true);
                topRunwayUpdate();
            }
    }

    // Opens up the Runway Creation Window
    @FXML
    void addRunwayButtonEvent(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/RunwayCreation.fxml"));
            Parent root = loader.load();
            RunwayCreationController ctrl = loader.getController();

            ctrl.setParentController(this);
            Stage stage = new Stage();
            stage.setTitle("Runway Creation");
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        updateTables();
    }

    @FXML
    void removeRunwayButtonEvent(ActionEvent event) {

        // Removes the runway that is selected
        String runwayName;
        if (runwaySelect.getSelectionModel().getSelectedItem() != null) {
            runwayName = runwaySelect.getSelectionModel().getSelectedItem();
            ObservableList<Runway> observableNewList = FXCollections.observableArrayList();
            String runwaySelected = runwaySelect.getSelectionModel().getSelectedItem();
            runwaySelect.getItems().clear();
            topRunawayPane.setVisible(false);

            for (Runway currentRunway : airport.getObservableRunwayList()) {
                if (!runwaySelected.equals(currentRunway.toString())) {
                    observableNewList.add(currentRunway);
                    runwaySelect.getItems().add(currentRunway.toString());
                }
            }
            airport.setObservableRunwayList(observableNewList);
            notify("RemoveRunwayEvent on " + runwayName);
        }

        updateTables();
    }

    @FXML
    void runwaySelectEvent(Event event) {
        if (runwaySelect.getSelectionModel().getSelectedItem() != null) {
            current = airport.getRunway(runwaySelect.getSelectionModel().getSelectedItem());
            this.sideOnTabEvent(event);
            this.topDownTabEvent(event);
            topRunawayPane.setVisible(true);
            this.updateTables();
        }
        /*
        String value = runwaySelect.getValue();
        lineTODA.setStroke(Color.BLACK);
        lineASDA.setStroke(Color.BLACK);
        lineTORA.setStroke(Color.BLACK);
        lineLDA.setStroke(Color.BLACK);

        int toda =  (int) current.getLeftRunway().getInitialParameters().getToda();
        int asda = (int) current.getLeftRunway().getInitialParameters().getAsda();
        int tora = (int) current.getLeftRunway().getInitialParameters().getTora();
        int lda = (int) current.getLeftRunway().getInitialParameters().getLda();

        lineTODA.setEndX(430);
        lineASDA.setEndX((int) asda* 430 / toda);
        lineTORA.setEndX((int) tora* 430 / toda);
        lineLDA.setEndX((int) lda* 430 / toda);

        designatorL.setText(current.getLeftRunway().getDesignator());
        designatorR.setText(current.getRightRunway().getDesignator());

        textTORA.setText("TORA: " + current.getLeftRunway().getInitialParameters().getTora());
        textLDA.setText("LDA: " + current.getLeftRunway().getInitialParameters().getLda());
        textTODA.setText("TODA: " + current.getLeftRunway().getInitialParameters().getToda());
        textASDA.setText("ASDA: " + current.getLeftRunway().getInitialParameters().getAsda());

        //System.out.println(value);
           */

    }

    public void updateTables() {
        //clears table
        topLtableView.getItems().clear();
        topRtableView.getItems().clear();

        //checks to see if there is a runway selected
        if (runwaySelect.getSelectionModel().getSelectedItem() != null) {
            Runway selectedRunway = airport.getRunway(runwaySelect.getSelectionModel().getSelectedItem());

            leftRParamLabel.setText("Runway " + selectedRunway.getLeftRunway().toString());
            rightRParamLabel.setText("Runway " + selectedRunway.getRightRunway().toString());

            topLtableView.getItems().add(selectedRunway.getLeftRunway().getInitialParameters());
            topRtableView.getItems().add(selectedRunway.getRightRunway().getInitialParameters());

            // checks to see it has recalculated parameters
            if (selectedRunway.getLeftRunway().getRecalculatedParameters() != null) {
                topLtableView.getItems().add(selectedRunway.getLeftRunway().getRecalculatedParameters());
                topRtableView.getItems().add(selectedRunway.getRightRunway().getRecalculatedParameters());
            }

        } else {
            leftRParamLabel.setText("Runway");
            rightRParamLabel.setText("Runway");
        }

        // Puts data
        leftToda.setCellValueFactory(cellData -> cellData.getValue().todaProperty().asObject());
        leftAsda.setCellValueFactory(cellData -> cellData.getValue().asdaProperty().asObject());
        leftTora.setCellValueFactory(cellData -> cellData.getValue().toraProperty().asObject());
        leftLda.setCellValueFactory(cellData -> cellData.getValue().ldaProperty().asObject());

        rightToda.setCellValueFactory(cellData -> cellData.getValue().todaProperty().asObject());
        rightAsda.setCellValueFactory(cellData -> cellData.getValue().asdaProperty().asObject());
        rightTora.setCellValueFactory(cellData -> cellData.getValue().toraProperty().asObject());
        rightLda.setCellValueFactory(cellData -> cellData.getValue().ldaProperty().asObject());

    }

    @FXML
    void airportSelectionEvent(ActionEvent event) {
        airportList.setItems( FXCollections.observableArrayList(xmlImporter.getAirports()));
    }


    // Opens up the Object window
    @FXML
    void addObjectToRunwayEvent(ActionEvent event) {

        if (runwaySelect.getSelectionModel().getSelectedItem() != null) {
            addObjButton.setDisable(true);
            runwaySelect.setDisable(true);
            Runway runway = airport.getRunway(runwaySelect.getSelectionModel().getSelectedItem());
            if (runway.getLeftRunway().getRecalculatedParameters() == null && runway.getRightRunway().getRecalculatedParameters() == null) {

                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ObjectCreation.fxml"));
                    Parent root = loader.load();
                    ObjectCreationController ctrl = loader.getController();

                    ctrl.getLeftThrLabel().setText("Displaced Threshold Runway " + airport.getRunway(runwaySelect.getSelectionModel().getSelectedItem()).getLeftRunway().toString());
                    ctrl.getRightThrLabel().setText("Displaced Threshold Runway " + airport.getRunway(runwaySelect.getSelectionModel().getSelectedItem()).getRightRunway().toString());

                    List<Obstacle> obstaclesList = xmlImporter.getObstacles();
                    ctrl.objectComboBox.setItems( FXCollections.observableArrayList(obstaclesList));
                    ctrl.setParentController(this);
                    Stage stage = new Stage();
                    stage.setOnCloseRequest(e -> {
                        runwaySelect.setDisable(false);
                        addObjButton.setDisable(false);
                    });
                    stage.setTitle("Object");
                    stage.setResizable(false);
                    stage.setScene(new Scene(root));
                    stage.show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                final Stage dialog = new Stage();
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.setFullScreen(false);
                dialog.setResizable(false);
                dialog.setTitle("Add Object");
                dialog.setIconified(false);
                VBox dialogVbox = new VBox(20);

                Text text = new Text("There already is an object on the runway." + "\n" + "Please remove it and try again");
                notify("Object creation error: There already is an object on the runway. Please remove it and try again");


                Scene dialogScene = new Scene(dialogVbox, 250, 80);
                dialog.setScene(dialogScene);
                dialogVbox.setPadding(new Insets(20, 20, 20, 20));
                dialogVbox.getChildren().add(text);
                dialog.show();
                addObjButton.setDisable(false);
                runwaySelect.setDisable(false);
            }
        } else {
            final Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setFullScreen(false);
            dialog.setResizable(false);
            dialog.setTitle("Add Object");
            dialog.setIconified(false);
            VBox dialogVbox = new VBox(20);

            Text text = new Text("No runway has been selected.\nPlease select a runway and try again.");
            notify("Object creation error: No runway has been selected. Please select a runway and try again.");


            Scene dialogScene = new Scene(dialogVbox, 250, 80);
            dialog.setScene(dialogScene);
            dialogVbox.setPadding(new Insets(20, 20, 20, 20));
            dialogVbox.getChildren().add(text);
            dialog.show();
            addObjButton.setDisable(false);
            runwaySelect.setDisable(false);
        }

    }

    @FXML
    void removeObjButtonEvent(ActionEvent event) {
        if (runwaySelect.getSelectionModel().getSelectedItem() != null && airport.getRunway(runwaySelect.getSelectionModel().getSelectedItem()).getObstacle() != null) {
            Runway runway = airport.getRunway(runwaySelect.getSelectionModel().getSelectedItem());
            runway.setObstacle(null);
            runway.getLeftRunway().setRecalculatedParameters(null);
            runway.getRightRunway().setRecalculatedParameters(null);
            updateTables();
            runwayUpdate();
            topRunwayUpdate();
            notify("Object removed from runway " + runway);
        } else if (runwaySelect.getSelectionModel().getSelectedItem() != null && airport.getRunway(runwaySelect.getSelectionModel().getSelectedItem()).getObstacle() == null) {
            Runway runway = airport.getRunway(runwaySelect.getSelectionModel().getSelectedItem());
            notify("No object on runway " + runway);
        } else {
            notify("No runway has been selected ");
        }

    }

    @FXML
    void topShowCalculationButtonEvent(ActionEvent event) {
        ShowCalculations("Top Button");
    }

    @FXML
    void BottomShowCalculationButtonEvent(ActionEvent event) {
        ShowCalculations("Bottom Button");
    }

    public void ShowCalculations(String button) {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        VBox dialogVbox = new VBox(20);
        Text text = new Text();

        if (runwaySelect.getSelectionModel().getSelectedItem() == null) {
            text.setText("No Runway Selected");
            text.setLayoutX(20);
        } else {
            VirtualRunway virtualRunway;
            Runway runway = airport.getRunway(runwaySelect.getSelectionModel().getSelectedItem());
            if (button.equals("Top Button"))
                virtualRunway = runway.getLeftRunway();
            else
                virtualRunway = runway.getRightRunway();

            if (virtualRunway.getRecalculatedParameters() == null)
                text.setText("There are no calculations to show.");
            else
                text.setText(virtualRunway.getRecalculatedParameters().getCalculationBrkdwn());
        }
        Scene dialogScene = new Scene(dialogVbox, 600, 300);
        dialog.setScene(dialogScene);

        dialogVbox.setPadding(new Insets(20, 20, 20, 20));
        dialogVbox.getChildren().add(text);
        dialog.setFullScreen(false);
        dialog.setResizable(false);
        dialog.show();
    }

    @FXML
    public void importEvent(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        fileChooser.setTitle("Load");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XML Files", "*.xml"));

        try {
            File file = fileChooser.showOpenDialog(dialog);

            if (file != null) {
                Airport a = xmlImporter.parseAirportFile(file.toPath());
                options.add(a);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void exportEvent(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        fileChooser.setTitle("Save");
        fileChooser.setInitialFileName("airport.xml");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XML Files", "*.xml"));

        try {
            File file = fileChooser.showSaveDialog(dialog);

            if (file != null) {
                var airportDoc = xmlExporter.exportXML(airport);
                TransformerFactory tf = TransformerFactory.newDefaultInstance();
                Transformer t = tf.newTransformer();
                t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
                t.setOutputProperty(OutputKeys.INDENT, "yes");
                t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
                DOMSource src = new DOMSource(airportDoc);
                FileWriter fw = new FileWriter(file);
                StreamResult res = new StreamResult(fw);
                t.transform(src, res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void xmlSelectionEvent(ActionEvent actionEvent) {
    }
}
