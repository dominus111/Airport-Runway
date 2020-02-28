package runway.Model;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Set;

public class Airport {

    private ObservableList<String> observableRunwayList;

    public Airport() {
        observableRunwayList = FXCollections.observableArrayList("Runway 1", "Runway 2", "Runway 3");

    }

    public ObservableList<String> getObservableRunwayList() {
        return observableRunwayList;
    }
}
