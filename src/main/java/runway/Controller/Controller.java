package runway.Controller;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
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
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.*;
import javafx.util.Callback;
import runway.Model.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    private Rectangle greenPart, bluePart;

    @FXML
    protected CheckBox todaCBox, toraCBox, asdaCBox, ldaCBox;

    @FXML
    protected Button addObjButton, addRunwayButton, removeRunwayButton, removeObjButton, popOutButton;
    @FXML
    protected Button topShowCalcButton, bottomShowCalcButton;
    @FXML
    protected Button rotateRadioButton;
    @FXML
    protected Line arrowLine;

    @FXML
    protected ImageView image, image1;
    @FXML
    protected Label lableDir, hideShowLable;

    @FXML
    private AnchorPane notificationBox;
    @FXML
    private AnchorPane sideOnAnchorPane;
    @FXML
    private AnchorPane topView, topRunawayPane;
    @FXML
    private Line lineTODA, lineASDA, lineTORA, lineLDA, centerLine, leftStart, rightStart;
    @FXML
    private Text textTODA, textASDA, textTORA, textLDA, colorText, colorTextSide;
    @FXML
    private Text designatorL, designatorR;
    @FXML
    private Polygon topLeftArrow, topRightArrow;
    @FXML
    private Rectangle topYellow, topRed, topOrange;
    @FXML
    private Circle obstacle;
    @FXML
    private CheckBox colorBlind, colorBlindSide;

    @FXML
    private Polyline topPoly, midPoly, bottomPoly;
    @FXML
    private RadioButton takingOffRadioButton, landingRadioButton;

    private double initialLength;
    /**
     * SIDE VIEW
     */

    @FXML
    private Text sideTORA, sideLDA, sideTODA, sideASDA, sideRESA;
    @FXML
    private RadioButton sideLeftButton, sideRightButton;
    @FXML
    protected RadioButton leftB, rightB;
    @FXML
    private Line sideLineTODA, sideLineLDA, sideLineTORA, sideLineASDA, sideLineRESA;
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
            removeObjButton.setDisable(true);
            bottomShowCalcButton.setDisable(true);
            topShowCalcButton.setDisable(true);
            disableButtons();
            disableViewButtons(true);
            makeGraphicsVisible(false);
            topLeftArrow.setVisible(false);
        }
    }

    public Airport getAirport() {
        return airport;
    }

    void setAllButtonsDisable(Boolean value) {
        airportList.setDisable(value);
        addObjButton.setDisable(value);
        addRunwayButton.setDisable(value);
        addRunwayButton.setDisable(value);
        topShowCalcButton.setDisable(value);
        bottomShowCalcButton.setDisable(value);
        popOutButton.setDisable(value);
        xmlImport.setDisable(value);
        xmlExport.setDisable(value);
        runwaySelect.setDisable(value);
        disableViewButtons(value);
        makeGraphicsVisible(value);

        if(runwaySelect.getSelectionModel().getSelectedItem() == null) {
            removeRunwayButton.setDisable(true);
            makeGraphicsVisible(false);
            disableViewButtons(true);
            addObjButton.setDisable(true);
            bottomShowCalcButton.setDisable(true);
            topShowCalcButton.setDisable(true);
        } else {
            removeRunwayButton.setDisable(false);
            disableViewButtons(false);
            makeGraphicsVisible(true);
            Runway current = airport.getRunway(runwaySelect.getSelectionModel().getSelectedItem());

            if(current.getObstacle() != null) {
                addObjButton.setDisable(true);
                bottomShowCalcButton.setDisable(false);
                topShowCalcButton.setDisable(false);
            }
            else {
                addObjButton.setDisable(false);
                bottomShowCalcButton.setDisable(true);
                topShowCalcButton.setDisable(true);
            }
        }

        if(airportList.getSelectionModel().getSelectedItem() == null) {
            runwaySelect.setDisable(true);
            addRunwayButton.setDisable(true);
        } else {
            runwaySelect.setDisable(false);
            addRunwayButton.setDisable(false);
        }
    }

    void disableButtons() {
        runwaySelect.setDisable(true);
        addRunwayButton.setDisable(true);
        addObjButton.setDisable(true);
        bottomShowCalcButton.setDisable(true);
        topShowCalcButton.setDisable(true);
        removeRunwayButton.setDisable(true);
    }

    public ComboBox<String> getRunwaySelect() {
        return runwaySelect;
    }
    Color yellow = Color.rgb(179,165,75);
    Color green = Color.rgb(111,176,99);
    Color blue = Color.rgb(179, 219, 255);

    @FXML
    void selectColorBlindness() {
        if (colorBlind.isSelected()) {
            topPoly.setFill(yellow);
            midPoly.setFill(yellow);
            bottomPoly.setFill(yellow);

            if (runwaySelect.getValue() != null) {
                topRunwayUpdate();
            }
        } else {
            topPoly.setFill(green);
            midPoly.setFill(green);
            bottomPoly.setFill(green);
            if (runwaySelect.getValue() != null) {
                topRunwayUpdate();
            }
        }
    }

    @FXML
    void selectColorBlindnessSide() {
        if(colorBlindSide.isSelected()) {
            greenPart.setFill(yellow);
            bluePart.setFill(Color.LIGHTGRAY);

        } else {
            greenPart.setFill(green);
            bluePart.setFill(blue);
        }
        if (runwaySelect.getValue() != null) {
            runwayUpdate();
        }
    }

    @FXML
    void mouseRed() {
        colorText.setText("This color represents the Clearway area");
        colorText.setVisible(true);
    }
    @FXML
    void mouseOrange() {
        colorText.setText("This color represents the Stopway area");
        colorText.setVisible(true);
    }
    @FXML
    void mouseYellow() {
        colorText.setText("This color represents the length from the start of the runway up to a place available for landing or taking off");
        colorText.setVisible(true);
    }
    @FXML
    void mouseObstacle() {
        colorText.setText("This is the obstacle");
        colorText.setVisible(true);
    }
    @FXML
    void mouseRExit() {
        colorTextSide.setVisible(false);
    }
    @FXML
    void mouseOExit() {
        colorText.setVisible(false);
    }
    @FXML
    void mouseYExit() {
        colorText.setVisible(false);
    }
    @FXML
    void mouseObsExit() {
        colorText.setVisible(false);
    }
    @FXML
    void mouseRedSide() {
        colorTextSide.setText("This color represents the Clearway area");
        colorTextSide.setVisible(true);
    }
    @FXML
    void mouseOrangeSide() {
        colorTextSide.setText("This color represents the Stopway area");
        colorTextSide.setVisible(true);
    }
    @FXML
    void mouseYellowSide() {
        colorTextSide.setText("This color represents the length from the start of the runway up to a place available for landing or taking off");
        colorTextSide.setVisible(true);
    }
    @FXML
    void mouseObstacleSide() {
        colorTextSide.setText("This is the obstacle");
        colorTextSide.setVisible(true);
    }
    @FXML
    void mouseRExitSide() {
        colorTextSide.setVisible(false);
    }
    @FXML
    void mouseOExitSide() {
        colorTextSide.setVisible(false);
    }
    @FXML
    void mouseYExitSide() {
        colorTextSide.setVisible(false);
    }
    @FXML
    void mouseObsExitSide() {
        colorTextSide.setVisible(false);
    }

    @FXML
    void showTODA(){
        if(lineTODA.isVisible()){
            lineTODA.setVisible(false);
            textTODA.setVisible(false);
        }
        else{
            lineTODA.setVisible(true);
            textTODA.setVisible(true);
        }
        if(sideLineTODA.isVisible()){
            sideLineTODA.setVisible(false);
            sideTODA.setVisible(false);
        }
        else{
            sideLineTODA.setVisible(true);
            sideTODA.setVisible(true);
        }
    }

    @FXML
    void showTORA(){
        if(lineTORA.isVisible()){
            lineTORA.setVisible(false);
            textTORA.setVisible(false);
        }
        else{
            lineTORA.setVisible(true);
            textTORA.setVisible(true);
        }
        if(sideLineTORA.isVisible()){
            sideLineTORA.setVisible(false);
            sideTORA.setVisible(false);
        }
        else{
            sideLineTORA.setVisible(true);
            sideTORA.setVisible(true);
        }
    }

    @FXML
    void showASDA(){
        if(lineASDA.isVisible()){
            lineASDA.setVisible(false);
            textASDA.setVisible(false);
        }
        else{
            lineASDA.setVisible(true);
            textASDA.setVisible(true);
        }
        if(sideLineASDA.isVisible()){
            sideLineASDA.setVisible(false);
            sideASDA.setVisible(false);
        }
        else{
            sideLineASDA.setVisible(true);
            sideASDA.setVisible(true);
        }
    }

    @FXML
    void showLDA(){
        if(lineLDA.isVisible()){
            lineLDA.setVisible(false);
            textLDA.setVisible(false);
        }
        else{
            lineLDA.setVisible(true);
            textLDA.setVisible(true);
        }
        if(sideLineLDA.isVisible()){
            sideLineLDA.setVisible(false);
            sideLDA.setVisible(false);
        }
        else{
            sideLineLDA.setVisible(true);
            sideLDA.setVisible(true);
        }
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
        String text = message + "  -  " + (new SimpleDateFormat("dd/MM/yyyy @ HH:mm:ss").format(new Date()));
        notifications.add(text);
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

    public void updateRotationLable() {
        if (!rotateRadioButton.getText().equals("Rotate to initial position")) {
            rotateRadioButton.setText("Match compas heading: " + getRotation());
        }
    }

    public void topLeftButton(ActionEvent actionEvent) {
        if (runwaySelect.getValue() != null) {
            leftView = true;
            topRunwayUpdate();
        }
        updateRotationLable();
    }

    public void topRightButton(ActionEvent actionEvent) {
        if (runwaySelect.getValue() != null) {
            leftView = false;
            topRunwayUpdate();
        }
        updateRotationLable();
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

    public int getRotation() {
        int rotate = 0;
        if(runwaySelect.getSelectionModel().getSelectedItem() != null) {
            Runway current = airport.getRunway(runwaySelect.getSelectionModel().getSelectedItem());

            if (leftView){
                String str = current.getLeftRunway().getDesignator();
                String value =  str.substring(0, str.length() - 1);
                rotate = Integer.parseInt(value) * 10;
                System.out.println(rotate);
            } else{
                String str = current.getRightRunway().getDesignator();
                String value =  str.substring(0, str.length() - 1);
                rotate = Integer.parseInt(value) * 10;
                System.out.println(rotate);
            }
        }
        return rotate;
    }

    @FXML
    void rotateEvent (ActionEvent e) {
            int rotate;
            rotate = getRotation();
            if (rotateRadioButton.getText().equals("Rotate to initial position")) {
                rotateToValue(90);
                rotateRadioButton.setText("Match compas heading: " + rotate);
            } else {

                if (rotate > 180 )
                    rotateToValue(rotate - 180);
                else
                    rotateToValue(rotate);

                rotateRadioButton.setText("Rotate to initial position");
        }

    }

    public void rotateToValue ( int value ) {
        int val;
        if(value >= 90)
            val = value - 90;
        else
            val = 360 - (90 - value);
        topRunawayPane.setRotate(val);
    }

    public void disableViewButtons ( boolean val)  {
        leftB.setVisible(!val);
        leftB.setDisable(val);
        rightB.setDisable(val);
        rightB.setVisible(!val);
        takingOffRadioButton.setDisable(val);
        takingOffRadioButton.setVisible(!val);
        landingRadioButton.setVisible(!val);
        landingRadioButton.setDisable(val);
        rotateRadioButton.setDisable(val);
        rotateRadioButton.setVisible(!val);
        ldaCBox.setDisable(val);
        ldaCBox.setVisible(!val);
        asdaCBox.setDisable(val);
        asdaCBox.setVisible(!val);
        toraCBox.setDisable(val);
        toraCBox.setVisible(!val);
        todaCBox.setDisable(val);
        todaCBox.setVisible(!val);
        hideShowLable.setVisible(!val);
        hideShowLable.setDisable(val);

        if(runwaySelect.getSelectionModel().getSelectedItem() != null) {
            Runway runway = airport.getRunway(runwaySelect.getSelectionModel().getSelectedItem());
            val = runway.getObstacle() == null;
            takingOffRadioButton.setDisable(val);
            takingOffRadioButton.setVisible(!val);
            landingRadioButton.setVisible(!val);
            landingRadioButton.setDisable(val);
        }
    }

    public void makeGraphicsVisible ( boolean val) {
        designatorR.setVisible(val);
        designatorL.setVisible(val);
        lableDir.setVisible(val);
        arrowLine.setVisible(val);
        lineTODA.setVisible(val);
        textTODA.setVisible(val);
        lineTORA.setVisible(val);
        textTORA.setVisible(val);
        lineASDA.setVisible(val);
        textASDA.setVisible(val);
        lineLDA.setVisible(val);
        textLDA.setVisible(val);

        if (leftB.isDisable()) {
            topLeftArrow.setVisible(false);
            topRightArrow.setVisible(false);
        }
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
        double obstOffDisp2 = 0;
        double obstSlope = 0;
        boolean boolObstacle = false;


        if( current.getObstacle() != null){
            obstacle.setVisible(true);
            obstHight = (int) current.getObstacle().getHeight();
            obstLTHR = (int) current.getObstacle().getoParam().getDistToLTHR();
            obstRTHR = (int) current.getObstacle().getoParam().getDistToRTHR();
            obstCLine = (int) current.getObstacle().getoParam().getDistToCenterL();
            obstDisp = obstHight * slope + RESA + stripEnd;
            obstOffDisp = obstLTHR - (RESA + stripEnd );
            obstOffDisp2 = obstLTHR - (RESA + stripEnd + (obstHight * slope));
            obstSlope = (RESA + stripEnd + (obstHight * slope));
            boolObstacle = true;
        }
        else{
            obstacle.setVisible(false);
            boolObstacle = false;
        }

        if(colorBlind.isSelected()) {
            topRed.setFill(Color.PURPLE);
            topOrange.setFill(Color.BLUE);
        } else {
            topRed.setFill(Color.RED);
            topOrange.setFill(Color.ORANGE);
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
            System.out.println("LDA IS " + lda);
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
                        lineLDA.setVisible(false);
                        textLDA.setVisible(false);
                        ldaCBox.setSelected(false);
                        ldaCBox.setDisable(true);
                        obstacle.setLayoutX(centerLine.getLayoutX() + obstoffset);

                        double ldaoffset2 = (TTODA * tora / toda) - (blast + obstoffset);
                        double asdaoffset = (TTODA * asda / toda) - (blast + obstoffset);
                        double toraoffset = (TTODA * tora / toda - (blast + obstoffset));

                        double yellowoffset = blast + obstoffset;
                        double redoffset = toraoffset + LTORA + (blast + obstoffset);
                        double orangeoffset = toraoffset + LTORA + (blast + obstoffset);

                        double ldaoffset = centerLine.getLayoutX() + obstoffset + blast;

                        topRunaway.setWidth(asdaoffset + TORA_ASDA_DIST + 4 + (blast + obstoffset));
                        centerLine.setEndX(toraoffset + (blast + obstoffset));
                        rightStart.setLayoutX(toraoffset + LTORA - lineLenght - 2 + (blast + obstoffset));

                        topYellow.setLayoutX(LCOLOR);
                        topYellow.setWidth(yellowoffset);

                        topRed.setLayoutX(redoffset);
                        topRed.setWidth(RTODA - redoffset);

                        topOrange.setLayoutX(orangeoffset);
                        topOrange.setWidth(asdaoffset - toraoffset);

                        lineTODA.setLayoutX(ldaoffset);
                        lineASDA.setLayoutX(ldaoffset);
                        lineTORA.setLayoutX(ldaoffset);
                        lineLDA.setLayoutX(ldaoffset);

                        lineTODA.setEndX(RTODA - LTORA- (blast+obstoffset));
                        lineASDA.setEndX(asdaoffset);
                        lineTORA.setEndX(toraoffset);
                        lineLDA.setEndX(ldaoffset2);

                        textTORA.setText("TORA: " + Math.round(tora - obstLTHR - BLAST) + "m");
                        textLDA.setText("LDA: " + Math.round(tora - obstLTHR - BLAST) + "m");
                        textTODA.setText("TODA: " + Math.round(toda - obstLTHR - BLAST) + "m");
                        textASDA.setText("ASDA: " + Math.round(asda - obstLTHR - BLAST) + "m");

                        /**
                         *  LANDING
                          */
                    } else {
                        lineLDA.setVisible(true);
                        textLDA.setVisible(true);
                        ldaCBox.setSelected(true);
                        ldaCBox.setDisable(false);
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
                    if (topTakingoff) {
                        double obstStart = centerLine.getLayoutX() + obstoffset;
                        obstacle.setLayoutX(obstStart);


                        double toraoffset = (TTODA * tora / toda);
                        double ldaoffset2 = (TTODA * obstOffDisp2 / toda) ;

                        double yellowoffset = toraoffset - ldaoffset2;
                        double redoffset = toraoffset + LTORA;
                        double orangeoffset = toraoffset + LTORA;

                        double toraoffset2 = (TTODA * obstOffDisp2 / toda);
                        double asdaoffset = (TTODA * obstOffDisp2 / toda) ;


                        double ldaoffset = LTORA;

                        topRunaway.setWidth(asdaoffset + TORA_ASDA_DIST + 4 + yellowoffset);
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
                        lineTORA.setEndX(toraoffset2);
                        lineLDA.setEndX(ldaoffset2);

                        textTORA.setText("TORA: " + Math.round(tora) + "m");
                        textLDA.setText("LDA: " + Math.round(obstOffDisp2) + "m");
                        textTODA.setText("TODA: " + Math.round(toda) + "m");
                        textASDA.setText("ASDA: " + Math.round(asda) + "m");
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
                textLDA.setText("LDA: " + Math.round(lda) + "m");
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

        if(colorBlindSide.isSelected()) {
            rightClearway.setFill(Color.PURPLE);
            rightStopway.setFill(Color.BLUE);
        } else {
            rightClearway.setFill(Color.RED);
            rightStopway.setFill(Color.ORANGE);
        }

        VirtualRunway v;
        boolean updated;
        sideLeftButton.setText(current.getLeftRunway().toString());
        sideRightButton.setText(current.getRightRunway().toString());
        sideRESA.setVisible(false);
        sideLineRESA.setVisible(false);
        sideLineTODA.setVisible(true);
        sideLineTORA.setVisible(true);
        sideLineASDA.setVisible(true);
        sideLineLDA.setVisible(true);

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
        if(updated && v.getRecalculatedParameters().getTora() > 0 && v.getRecalculatedParameters().getToda() > 0 && v.getRecalculatedParameters().getAsda() > 0
                && v.getRecalculatedParameters().getLda() > 0 && v.getRecalculatedParameters().getdispTHR() > 0) {
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

        sideTORA.setText("TORA: " + Math.round(tora) + "m");
        sideLDA.setText("LDA: " + Math.round(lda) + "m");
        sideTODA.setText("TODA: " + Math.round(toda) + "m");
        sideASDA.setText("ASDA: " + Math.round(asda) + "m");

        sideLineTORA.setStartX(PIXEL_START);
        sideLineLDA.setStartX(PIXEL_START + Double.max(PIXEL_TOTAL * (displacedThreshold / max), translate));
        sideLineTODA.setStartX(PIXEL_START);
        sideLineASDA.setStartX(PIXEL_START);

        sideLineTORA.setEndX(PIXEL_START + PIXEL_TOTAL * (tora / max));
        sideLineLDA.setEndX(PIXEL_START + PIXEL_TOTAL * (lda / max) + Double.max(PIXEL_TOTAL * ((displacedThreshold) / max), translate));
        sideLineTODA.setEndX(PIXEL_START + PIXEL_TOTAL * (toda / max));
        sideLineASDA.setEndX(PIXEL_START + PIXEL_TOTAL * (asda / max));

        //any setTranslateX using PIXEL_START must take into account the actual pixel start of -317
        sideDisplacedThreshold.setTranslateX( PIXEL_START + 317 );
        sideDisplacedThreshold.setWidth(PIXEL_TOTAL * (displacedThreshold / max));

        rightClearway.setTranslateX(PIXEL_START + 317 + PIXEL_TOTAL * (tora / max) + translate);
        rightClearway.setWidth(PIXEL_TOTAL * ((toda - tora) / max) - translate);

        rightStopway.setTranslateX(PIXEL_START + 317 + PIXEL_TOTAL * (tora / max) + translate);
        rightStopway.setWidth(PIXEL_TOTAL * ((asda - tora) / max) - translate);

        if(current.getObstacle() != null){
            sideObstacle.setVisible(true);
            sideRESA.setVisible(true);
            sideLineRESA.setVisible(true);

            if(leftView) {
                translate = PIXEL_TOTAL * (current.getObstacle().getoParam().getDistToLTHR() / max);
            }
            else{
                translate = PIXEL_TOTAL * (current.getObstacle().getoParam().getDistToRTHR() / max);
            }
            sideObstacle.setTranslateX(PIXEL_START + 317 + translate);
            translate += sideObstacle.getWidth();
            sideObstacle.setTranslateY(20 - current.getObstacle().getHeight());
            sideObstacle.setHeight(current.getObstacle().getHeight());

            sideLineLDA.setStartX(PIXEL_START + Double.max(PIXEL_TOTAL * (displacedThreshold / max), translate));
            sideLineLDA.setEndX(PIXEL_START + PIXEL_TOTAL * (lda / max) + Double.max(PIXEL_TOTAL * ((displacedThreshold) / max), translate));

            rightClearway.setTranslateX(PIXEL_START + 317 + PIXEL_TOTAL * (tora / max) + translate);
            rightClearway.setWidth(PIXEL_TOTAL * ((toda - tora) / max) - translate);

            rightStopway.setTranslateX(PIXEL_START + 317 + PIXEL_TOTAL * (tora / max) + translate);
            rightStopway.setWidth(PIXEL_TOTAL * ((asda - tora) / max) - translate);

        }else{
            sideObstacle.setVisible(false);
            sideRESA.setVisible(false);
            sideLineRESA.setVisible(false);
        }

//        if( current.getObstacle() != null){
//            sideObstacle.setVisible(true);
//            sideRESA.setVisible(true);
//            sideLineRESA.setVisible(true);
//            if(leftView) {
//                translate = PIXEL_TOTAL * (current.getObstacle().getoParam().getDistToLTHR() / max);
//            }
//            else{
//                translate = PIXEL_TOTAL * (current.getObstacle().getoParam().getDistToRTHR() / max);
//            }
//            sideObstacle.setTranslateX(PIXEL_START + 317 + translate);
//            translate += sideObstacle.getWidth();
//            sideObstacle.setTranslateY(10 - current.getObstacle().getHeight());
//            sideObstacle.setHeight(current.getObstacle().getHeight());
//        }
//        else{
//            sideObstacle.setVisible(false);
//            sideRESA.setVisible(false);
//            sideLineRESA.setVisible(false);
//        }
//
//
//        sideTORA.setText("TORA: " + Math.round(tora) + "m");
//        sideLineTORA.setStartX(PIXEL_START - 20);
//        sideLineTORA.setEndX(PIXEL_START + PIXEL_TOTAL * (tora / max));
//

//
//        sideLDA.setText("LDA: " + Math.round(lda) + "m");
//        sideLineLDA.setStartX(PIXEL_START + Double.max(PIXEL_TOTAL * (displacedThreshold / max), translate));
//        sideLineLDA.setEndX(PIXEL_START + PIXEL_TOTAL * (lda / max) + Double.max(PIXEL_TOTAL * ((displacedThreshold) / max), translate));
//
//
//
//        sideTODA.setText("TODA: " + Math.round(toda) + "m");
//        sideLineTODA.setStartX(PIXEL_START);
//        sideLineTODA.setEndX(PIXEL_START + PIXEL_TOTAL * (toda / max));
//
//        sideASDA.setText("ASDA: " + Math.round(asda) + "m");
//        sideLineASDA.setStartX(PIXEL_START);
//        sideLineASDA.setEndX(PIXEL_START + PIXEL_TOTAL * (asda / max));
//
//        if(sideLineLDA.getStartX() < PIXEL_START || sideLineLDA.getStartX() > PIXEL_END){
//            sideLineLDA.setStartX(PIXEL_START);
//        }
//        if(sideLineASDA.getStartX() < PIXEL_START || sideLineASDA.getStartX() > PIXEL_END){
//            sideLineASDA.setStartX(PIXEL_START);
//        }
//        if(sideLineTODA.getStartX() < PIXEL_START || sideLineTODA.getStartX() > PIXEL_END){
//            sideLineTODA.setStartX(PIXEL_START);
//        }
//        if(sideLineTORA.getStartX() < PIXEL_START || sideLineTORA.getStartX() > PIXEL_END){
//            sideLineTORA.setStartX(PIXEL_START);
//        }
//        if(sideLineTORA.getEndX() > PIXEL_END || sideLineTORA.getStartX() < PIXEL_START){
//            sideLineTORA.setEndX(PIXEL_END);
//        }
//        if(sideLineTODA.getEndX() > PIXEL_END || sideLineTODA.getStartX() < PIXEL_START){
//            sideLineTODA.setEndX(PIXEL_END);
//        }
//        if(sideLineLDA.getEndX() > PIXEL_END || sideLineLDA.getStartX() < PIXEL_START){
//            sideLineLDA.setEndX(PIXEL_END);
//        }
//        if(sideLineASDA.getEndX() > PIXEL_END || sideLineASDA.getStartX() < PIXEL_START){
//            sideLineASDA.setEndX(PIXEL_END);
//        }
        sideOnAnchorPane.setVisible(true);
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
            stage.setOnCloseRequest(e -> {
                makeGraphicsVisible(true);
                disableViewButtons(false);
                setAllButtonsDisable(false);
                runwaySelectEvent(e);
            });
            stage.show();

            setAllButtonsDisable(true);
            disableButtons();
            disableViewButtons(true);
            makeGraphicsVisible(false);
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
            sideOnAnchorPane.setVisible(false);

            for (Runway currentRunway : airport.getObservableRunwayList()) {
                if (!runwaySelected.equals(currentRunway.toString())) {
                    observableNewList.add(currentRunway);
                    runwaySelect.getItems().add(currentRunway.toString());
                }
            }
            airport.setObservableRunwayList(observableNewList);
            notify("RemoveRunwayEvent on " + runwayName);
            removeRunwayButton.setDisable(true);
            removeObjButton.setDisable(true);
            removeObjButton.setText("No object on Runway");
            addObjButton.setDisable(true);
            bottomShowCalcButton.setDisable(true);
            topShowCalcButton.setDisable(true);
            disableViewButtons(true);
            makeGraphicsVisible(false);
        } else {
            notify("Nothing to remove. No runway has been selected.");
        }

        updateTables();
    }

    @FXML
    void runwaySelectEvent(Event event) {
        if(rotateRadioButton.getText().equals("Rotate to initial position"))
            rotateEvent(new ActionEvent());
        if (runwaySelect.getSelectionModel().getSelectedItem() != null) {
            current = airport.getRunway(runwaySelect.getSelectionModel().getSelectedItem());
            removeRunwayButton.setDisable(false);
            if(current != null ) {
                if (current.getObstacle() != null) {
                    removeObjButton.setDisable(false);
                    addObjButton.setDisable(true);
                    bottomShowCalcButton.setDisable(false);
                    topShowCalcButton.setDisable(false);
                    removeObjButton.setText("Remove Object " + current.getObstacle().getName());
                } else {
                    removeObjButton.setDisable(true);
                    addObjButton.setDisable(false);
                    bottomShowCalcButton.setDisable(true);
                    topShowCalcButton.setDisable(true);
                    removeObjButton.setText("No object on Runway");
                }
            }
            this.sideOnTabEvent(event);
            this.topDownTabEvent(event);
            topRunawayPane.setVisible(true);
            sideOnAnchorPane.setVisible(true);
            this.updateTables();
            disableViewButtons(false);
            makeGraphicsVisible(true);
            topLeftButton(new ActionEvent());
            leftB.fire();
            if(runwaySelect.getSelectionModel().getSelectedItem() != null)
                rotateRadioButton.setText("Match compas heading: " + getRotation());
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

    @FXML
    void airportSelectEvent(ActionEvent event) {
        if(airportList.getSelectionModel().getSelectedItem() != null) {
            airport = airportList.getSelectionModel().getSelectedItem();
            Airport current = airportList.getSelectionModel().getSelectedItem();
            if (runwaySelect != null) {
                runwaySelect.getItems().clear();
                for (Runway currentRunway : current.getObservableRunwayList()) {
                    runwaySelect.getItems().add(currentRunway.toString());
                    currentRunway.setObstacle(null);
                }
            }
            addRunwayButton.setDisable(false);
            runwaySelect.setDisable(false);

            sideOnAnchorPane.setVisible(false);
            topRunawayPane.setVisible(false);
        }

    }
    // Opens up the Object window
    @FXML
    void addObjectToRunwayEvent(ActionEvent event) {

        if (runwaySelect.getSelectionModel().getSelectedItem() != null) {
            setAllButtonsDisable(true);
            disableButtons();
            makeGraphicsVisible(false);
            disableViewButtons(true);

            if(rotateRadioButton.getText().equals("Rotate to initial position"))
                rotateEvent(event);

            Runway runway = airport.getRunway(runwaySelect.getSelectionModel().getSelectedItem());

            if (runway.getLeftRunway().getRecalculatedParameters() == null && runway.getRightRunway().getRecalculatedParameters() == null) {

                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ObjectCreation.fxml"));
                    Parent root = loader.load();
                    ObjectCreationController ctrl = loader.getController();
                    ctrl.setRunway(current);

                    ctrl.getLeftThrLabel().setText("Distance to Threshold Runway " + airport.getRunway(runwaySelect.getSelectionModel().getSelectedItem()).getLeftRunway().toString());
                    ctrl.getRightThrLabel().setText("Distance to Threshold Runway " + airport.getRunway(runwaySelect.getSelectionModel().getSelectedItem()).getRightRunway().toString());

                    List<Obstacle> obstaclesList = xmlImporter.getObstacles();
                    ctrl.objectComboBox.setItems( FXCollections.observableArrayList(obstaclesList));
                    ctrl.setParentController(this);
                    Stage stage = new Stage();
                    stage.setOnCloseRequest(e -> {
                        makeGraphicsVisible(true);
                        disableViewButtons(false);
                        setAllButtonsDisable(false);
                        runwaySelectEvent(e);
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
                notify("There already is an object on the runway. Please remove it and try again");

                Scene dialogScene = new Scene(dialogVbox, 250, 80);
                dialog.setScene(dialogScene);
                dialogVbox.setPadding(new Insets(20, 20, 20, 20));
                dialogVbox.getChildren().add(text);
                dialog.show();
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
            notify("No runway has been selected. Please select a runway and try again.");

            Scene dialogScene = new Scene(dialogVbox, 250, 80);
            dialog.setScene(dialogScene);
            dialogVbox.setPadding(new Insets(20, 20, 20, 20));
            dialogVbox.getChildren().add(text);
            dialog.show();
        }


        if(runwaySelect.getSelectionModel().getSelectedItem() != null) {
            Runway current = airport.getRunway(runwaySelect.getSelectionModel().getSelectedItem());
            if(current.getObstacle() != null) {
                removeObjButton.setDisable(false);
                bottomShowCalcButton.setDisable(false);
                topShowCalcButton.setDisable(false);
                removeObjButton.setText("Remove Object " + current.getObstacle().getName());
            } else {
                removeObjButton.setDisable(true);
                bottomShowCalcButton.setDisable(true);
                topShowCalcButton.setDisable(true);
                removeObjButton.setText("No object on Runway");
            }
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
            if(rotateRadioButton.getText().equals("Rotate to initial position"))
                rotateEvent(event);
            topRunwayUpdate();
            notify("Object removed from runway " + runway + ".");
            addObjButton.setDisable(false);
            bottomShowCalcButton.setDisable(true);
            topShowCalcButton.setDisable(true);
            removeObjButton.setDisable(true);
            removeObjButton.setText("No object on Runway");
        } else if (runwaySelect.getSelectionModel().getSelectedItem() != null && airport.getRunway(runwaySelect.getSelectionModel().getSelectedItem()).getObstacle() == null) {
            Runway runway = airport.getRunway(runwaySelect.getSelectionModel().getSelectedItem());
            notify("No object on runway " + runway + ".");
        } else {
            notify("No object to remove. No runway has been selected.");
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
        setAllButtonsDisable(true);
        disableButtons();
        makeGraphicsVisible(false);
        disableViewButtons(true);
        FileChooser fileChooser = new FileChooser();
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        fileChooser.setTitle("Load");
        dialog.setOnCloseRequest(e -> {
            makeGraphicsVisible(true);
            disableViewButtons(false);
            setAllButtonsDisable(false);
            runwaySelectEvent(e);
        });

        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
        try {
            File file = fileChooser.showOpenDialog(dialog);

            if (file != null) {
                Airport a = xmlImporter.parseAirportFile(file.toPath());
                options.add(a);
                setAllButtonsDisable(false);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        setAllButtonsDisable(false);
    }

    @FXML
    public void exportEvent(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setOnCloseRequest(e -> {
            makeGraphicsVisible(true);
            disableViewButtons(false);
            setAllButtonsDisable(false);
            runwaySelectEvent(e);
        });

        fileChooser.setTitle("Save");
        fileChooser.setInitialFileName("airport.xml");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
        setAllButtonsDisable(true);
        disableButtons();
        makeGraphicsVisible(false);
        disableViewButtons(true);
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
        setAllButtonsDisable(false);
    }

    protected String checkDouble(TextField textField, String lastValue, int lowerVal, int upperVal, int maxLength) {
        String string = textField.getText();
        String cleanString = "";

        if (string.length() > 0) {

            for(int i = 0 ; i < string.length() ; i++)
                if(string.charAt(i) == '.') {
                    cleanString = string.substring(0, i) + '.' + string.substring(i).replaceAll("[^0-9]", "");
                    string = cleanString;
                    i = string.length();
                }

            if(string.length()>0)
                if (string.charAt(0) == '-' && lowerVal < 0) {
                    cleanString = "-" + string.replaceAll("[^0-9.]", "");
                } else
                    cleanString = string.replaceAll("[^0-9.]", "");

                if (cleanString.length() > maxLength)
                    cleanString = cleanString.substring(0,maxLength);

            try {
                double value = Double.parseDouble(cleanString);
                if (value < lowerVal || value > upperVal || string.length() > 12) {
                    textField.setText(lastValue);
                    textField.positionCaret(lastValue.length());
                    return lastValue;
                } else {
                    textField.setText(cleanString);
                    textField.positionCaret(cleanString.length());
                    return cleanString;
                }

            } catch (NumberFormatException e) {
                textField.setText(cleanString);
                textField.positionCaret(cleanString.length());
                return cleanString;
            }
        }
        return "";
    }

    public void xmlSelectionEvent(ActionEvent actionEvent) {
    }
}
