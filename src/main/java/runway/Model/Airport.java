package runway.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;

public class Airport {


    private ObservableList<Runway> observableRunwayList;
    private String name;

    public Airport() {
        name = "Heathrow Airport";
        observableRunwayList = FXCollections.observableArrayList();
        //Runway mock = new Runway(new VirtualRunway("09R", new RunwayParameters(4000,4000,4000,3500, 307)), new VirtualRunway("27L", new RunwayParameters(4000,4000,4000,4000, 0)));

        Runway mock = new Runway(new VirtualRunway("09R", new RunwayParameters(3660, 4000, 3800, 3353, 307)), new VirtualRunway("27L", new RunwayParameters(3660, 3660, 3660, 3660, 0)));

        Obstacle obstacle = new Obstacle("mock", 25);
        ObstaclePositionParam oParam = new ObstaclePositionParam(obstacle, 2853, 500, 20);

        Calculator calculator = new Calculator();
        calculator.calculate(oParam, mock);

        //observableRunwayList.add(new Runway(new VirtualRunway("09R", new RunwayParameters(3660,4000,3800,3353, 307)), new VirtualRunway("27L", new RunwayParameters(3660,3660,3660,3660, 0))));
        //observableRunwayList.add(new Runway(new VirtualRunway("09L", new RunwayParameters(3902,3902,3902,3595, 306)), new VirtualRunway("27R", new RunwayParameters(3884,3962,3884,3884, 0))));
        observableRunwayList.add(new Runway(new VirtualRunway("09L", new RunwayParameters(3902, 8000, 3902, 3595, 306)), new VirtualRunway("27R", new RunwayParameters(3000, 8000, 3700, 2500, 0))));

        observableRunwayList.add(mock);
    }

    public Airport(String name) {
        this.name = name;
        observableRunwayList = FXCollections.observableArrayList();
    }

    public ObservableList<Runway> getObservableRunwayList() {
        return observableRunwayList;
    }

    public void setObservableRunwayList(ObservableList<Runway> observableRunwayList) {
        this.observableRunwayList = observableRunwayList;
    }

    public void addRunway(Runway runway) {
        this.observableRunwayList.add(runway);
    }

    public Runway getRunway(String runwayName) {
        for (Runway currentRunway : this.getObservableRunwayList()) {
            if (runwayName.equals(currentRunway.toString())) {
                return currentRunway;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }

    public boolean isSame(Airport obj) {
        if (!name.equals(obj.getName())) return false;
        if (observableRunwayList.size() != obj.observableRunwayList.size()) return false;
        for (var i = 0; i < observableRunwayList.size(); i++) {
            var r1 = observableRunwayList.get(i);
            var r2 = obj.observableRunwayList.get(i);
            if (!r1.isSame(r2)) return false;
        }
        return true;
    }
}