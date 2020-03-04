package runway.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.AnchorPane;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import runway.Model.Airport;
import runway.Model.Runway;
import runway.Model.VirtualRunway;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ListIterator;

public class Controller {

    @FXML
    private ComboBox<String> runwaySelect;
    private Airport airport;
    private Runway current;
    @FXML
    private AnchorPane sideOnAnchorPane;
    @FXML
    private AnchorPane topView;
    @FXML
    private Line lineTODA;
    @FXML
    private Line lineASDA;
    @FXML
    private Line lineTORA;
    @FXML
    private Line lineLDA;
    @FXML
    private Text textTODA;
    @FXML
    private Text textASDA;
    @FXML
    private Text textTORA;
    @FXML
    private Text textLDA;
    @FXML
    private Text designatorL;
    @FXML
    private Text designatorR;

    private Boolean leftView = true;

    @FXML
    private Text sideTORA ,sideLDA, sideTODA ,sideASDA;
    @FXML
    private RadioButton sideLeftButton, sideRightButton;
    @FXML
    private RadioButton leftB, rightB;
    @FXML
    private Line sideLineTODA, sideLineLDA, sideLineTORA, sideLineASDA;
    @FXML
    private Rectangle rightStopway, rightClearway, sideDisplacedThreshold;


    @FXML
    public void initialize() {
        airport = new Airport();

        if(runwaySelect != null) {
            runwaySelect.getItems().clear();
            for(Runway currentRunway : airport.getObservableRunwayList()) {
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
    void sideOnTabEvent(Event event) {
        if(runwaySelect.getValue() != null){
            sideOnAnchorPane.setVisible(true);
            runwayUpdate();
        }
    }


    public void leftSideButtonClick(ActionEvent actionEvent) {
        if(runwaySelect.getValue() != null) {
            leftView = true;
            runwayUpdate();
        }
    }

    public void rightSideButtonClick(ActionEvent actionEvent) {
        if(runwaySelect.getValue() != null) {
            leftView = false;
            runwayUpdate();
        }
    }

    public void topLeftButton(ActionEvent actionEvent) {
        if(runwaySelect.getValue() != null) {
            leftView = true;
            topRunwayUpdate();
        }
    }

    public void topRightButton(ActionEvent actionEvent) {
        if(runwaySelect.getValue() != null) {
            leftView = false;
            topRunwayUpdate();
        }
    }

    public void topRunwayUpdate() {
        VirtualRunway v;
        leftB.setText(current.getLeftRunway().toString());
        rightB.setText(current.getRightRunway().toString());

        if (leftView) {
            v = current.getLeftRunway();
            designatorL.setText(current.getLeftRunway().getDesignator());
            designatorR.setText(current.getRightRunway().getDesignator());
        } else {
            v = current.getRightRunway();
            designatorR.setText(current.getLeftRunway().getDesignator());
            designatorL.setText(current.getRightRunway().getDesignator());
        }
        Integer PIXEL_START = -317;
        Integer PIXEL_TOTAL = 635;

        lineTODA.setStroke(Color.BLACK);
        lineASDA.setStroke(Color.BLACK);
        lineTORA.setStroke(Color.BLACK);
        lineLDA.setStroke(Color.BLACK);

        double tora = v.getInitialParameters().getTora();
        double lda = v.getInitialParameters().getLda();
        double toda = v.getInitialParameters().getToda();
        double asda = v.getInitialParameters().getAsda();

        lineTODA.setEndX(430);
        lineASDA.setEndX((int) asda* 430 / toda);
        lineTORA.setEndX((int) tora* 430 / toda);
        lineLDA.setEndX((int) lda* 430 / toda);


        textTORA.setText("TORA: " + tora);
        textLDA.setText("LDA: " + lda);
        textTODA.setText("TODA: " + toda);
        textASDA.setText("ASDA: " + asda);
    }



    public void runwayUpdate(){
        VirtualRunway v;
        sideLeftButton.setText(current.getLeftRunway().toString());
        sideRightButton.setText(current.getRightRunway().toString());

        if(leftView){
            v = current.getLeftRunway();
        }
        else{
            v = current.getRightRunway();
        }
        Integer PIXEL_START = -317;
        Integer PIXEL_TOTAL = 632 - 100;
        double tora = v.getInitialParameters().getTora();
        double lda = v.getInitialParameters().getLda();
        double toda = v.getInitialParameters().getToda();
        double asda = v.getInitialParameters().getAsda();
        double displacedThreshold = v.getInitialParameters().getDisplacedThreshold();

        double max = scale(tora,toda,lda,asda);

        sideTORA.setText("TORA: " + tora);
        sideLineTORA.setStartX(PIXEL_START);
        sideLineTORA.setEndX(PIXEL_START + PIXEL_TOTAL*(tora/max));

        rightClearway.setTranslateX(PIXEL_TOTAL*(tora/max));

        rightStopway.setTranslateX(PIXEL_TOTAL*(tora/max));

        sideLDA.setText("LDA: " + lda);
        sideLineLDA.setStartX(PIXEL_START + PIXEL_TOTAL*(displacedThreshold/max));
        sideLineLDA.setEndX(PIXEL_START + PIXEL_TOTAL*((lda + displacedThreshold)/max));

        sideDisplacedThreshold.setWidth(PIXEL_TOTAL*((tora - lda)/max));

        sideTODA.setText("TODA: " + toda);
        sideLineTODA.setStartX(PIXEL_START);
        sideLineTODA.setEndX(PIXEL_START + PIXEL_TOTAL*(toda/max));

        rightClearway.setWidth(PIXEL_TOTAL*((toda - tora)/max));

        sideASDA.setText("ASDA: " + asda);
        sideLineASDA.setStartX(PIXEL_START);
        sideLineASDA.setEndX(PIXEL_START + PIXEL_TOTAL*(asda/max));

        rightStopway.setWidth(PIXEL_TOTAL*((asda - tora)/max));
    }

    public double scale(double tora, double toda, double lda, double asda){
        double[] scalers = {tora,toda,lda,asda};
        double max = 0;
        for(double d : scalers){
            if(d>max){
                max = d;
            }
        }
        return max;
    }

    @FXML
    void topDownTabEvent(Event event) {
        if (runwaySelect != null)
        if(runwaySelect.getValue() != null) {
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
            stage.setScene(new Scene(root, 295, 585));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void removeRunwayButtonEvent(ActionEvent event) {

        // Removes the runway that is selected
        if (runwaySelect.getSelectionModel().getSelectedItem() != null) {
            ObservableList<Runway> observableNewList = FXCollections.observableArrayList();
            String runwaySelected = runwaySelect.getSelectionModel().getSelectedItem();
            runwaySelect.getItems().clear();

            for (Runway currentRunway : airport.getObservableRunwayList()) {
                if (!runwaySelected.equals(currentRunway.toString())) {
                    observableNewList.add(currentRunway);
                    runwaySelect.getItems().add(currentRunway.toString());
                }
            }

            airport.setObservableRunwayList(observableNewList);
        }
    }

    @FXML
    void runwaySelectEvent(Event event) {
        current = airport.getRunway(runwaySelect.getSelectionModel().getSelectedItem());
        this.sideOnTabEvent(event);
        this.topDownTabEvent(event);
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



    // Opens up the Object window
    @FXML
    void addObjectToRunwayEvent(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ObjectCreation.fxml"));
            Parent root = loader.load();
            ObjectCreationController ctrl = loader.getController();

            ctrl.setParentController(this);
            Stage stage = new Stage();
            stage.setTitle("Object");
            stage.setResizable(false);
            stage.setScene(new Scene(root, 256, 556));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
