package runway.Model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class RunwayParameters {

    private DoubleProperty tora, toda, asda, lda, displacedThreshold;

    public RunwayParameters(double tora, double toda, double asda, double lda, double displacedThreshold) {
        this.tora = new SimpleDoubleProperty(tora);
        this.toda = new SimpleDoubleProperty(toda);
        this.asda = new SimpleDoubleProperty(asda);
        this.lda = new SimpleDoubleProperty(lda);
        this.displacedThreshold = new SimpleDoubleProperty(displacedThreshold);
    }

    public DoubleProperty ldaProperty() {
        return lda;
    }

    public double getLda() {
        return ldaProperty().get();
    }

    public void setLda(double lda) {
        ldaProperty().set(lda);
    }
    public DoubleProperty todaProperty() {
        return toda;
    }

    public double getToda() {
        return todaProperty().get();
    }

    public void setToda(double toda) {
        todaProperty().set(toda);
    }
    public DoubleProperty asdaProperty() {
        return asda;
    }

    public double getAsda() {
        return asdaProperty().get();
    }

    public void setAsda(double asda) {
        asdaProperty().set(asda);
    }
    public DoubleProperty toraProperty() {
        return tora;
    }

    public double getTora() {
        return toraProperty().get();
    }

    public void setTora(double tora) {
        todaProperty().set(tora);
    }
    public DoubleProperty dispTHRProperty() {
        return displacedThreshold;
    }

    public double getdispTHR() {
        return dispTHRProperty().get();
    }

    public void setLDA(double displacedTHR) {
        dispTHRProperty().set(displacedTHR);
    }
}
