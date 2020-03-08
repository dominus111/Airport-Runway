package runway.Model;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.converter.PaintConverter;

import java.util.ArrayList;
import java.util.Set;

public class Airport {

    private ObservableList<Runway> observableRunwayList;

    public Airport() {
        observableRunwayList = FXCollections.observableArrayList();
        Runway mock = new Runway(new VirtualRunway("09R", new RunwayParameters(3660,4000,3800,3353, 307)), new VirtualRunway("27L", new RunwayParameters(3660,3660,3660,3660, 0)));

        Obstacle obstacle = new Obstacle("mock",25);
        ObstaclePositionParam oParam = new ObstaclePositionParam(obstacle, 2853,500,20);

        Calculator calculator = new Calculator();
        calculator.calculate(oParam, mock);

        //observableRunwayList.add(new Runway(new VirtualRunway("09R", new RunwayParameters(3660,4000,3800,3353, 307)), new VirtualRunway("27L", new RunwayParameters(3660,3660,3660,3660, 0))));
        observableRunwayList.add(new Runway(new VirtualRunway("09L", new RunwayParameters(3902,3902,3902,3595, 306)), new VirtualRunway("27R", new RunwayParameters(3884,3962,3884,3884, 0))));

        observableRunwayList.add(mock);
    }

    public ObservableList<Runway> getObservableRunwayList() {
        return observableRunwayList;
    }

    public void setObservableRunwayList(ObservableList<Runway> observableRunwayList) {
        this.observableRunwayList = observableRunwayList;
    }

    public void addRunway (Runway runway) {
        this.observableRunwayList.add(runway);
    }

    public Runway getRunway(String runwayName){
        for (Runway currentRunway : this.getObservableRunwayList()) {
            if (runwayName.equals(currentRunway.toString())) {
                return currentRunway;
            }
        }
        return null;
    }
}
