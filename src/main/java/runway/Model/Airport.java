package runway.Model;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Set;

public class Airport {

    private ObservableList<Runway> observableRunwayList;

    public Airport() {
        observableRunwayList = FXCollections.observableArrayList();
        observableRunwayList.add(new Runway(new VirtualRunway("09R", new RunwayParameters(3660,4000,3800,3353, 307)), new VirtualRunway("27L", new RunwayParameters(3660,3660,3660,3660, 0))));
        observableRunwayList.add(new Runway(new VirtualRunway("09L", new RunwayParameters(3902,4100,4000,3595, 306)), new VirtualRunway("27R", new RunwayParameters(3884,3962,3884,3884, 0))));
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
