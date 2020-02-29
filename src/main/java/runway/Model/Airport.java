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
    }

    public ObservableList<Runway> getObservableRunwayList() {
        return observableRunwayList;
    }

    public void addRunway (Runway runway) {
        this.observableRunwayList.add(runway);
    }
}
