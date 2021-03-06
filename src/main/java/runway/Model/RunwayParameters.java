package runway.Model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class RunwayParameters {

    private DoubleProperty tora, toda, asda, lda, displacedThreshold;
    private String calculationBrkdwn;

    public RunwayParameters(double tora, double toda, double asda, double lda, double displacedThreshold) {
        this.tora = new SimpleDoubleProperty(tora);
        this.toda = new SimpleDoubleProperty(toda);
        this.asda = new SimpleDoubleProperty(asda);
        this.lda = new SimpleDoubleProperty(lda);
        this.displacedThreshold = new SimpleDoubleProperty(displacedThreshold);
    }

    public String getCalculationBrkdwn() {
        return calculationBrkdwn;
    }

    public void setCalculationBrkdwn(String calculationBrkdwn) {
        this.calculationBrkdwn = calculationBrkdwn;
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

    public boolean isSame(RunwayParameters obj) {
        if (!tora.getValue().equals(obj.tora.getValue())) return false;
        if (!toda.getValue().equals(obj.toda.getValue())) return false;
        if (!asda.getValue().equals(obj.asda.getValue())) return false;
        if (!lda.getValue().equals(obj.lda.getValue())) return false;
        if (!displacedThreshold.getValue().equals(obj.displacedThreshold.getValue())) return false;
        return true;
    }

    @Override
    public String toString() {
        return  "tora: " + tora.getValue() +
                "\ntoda: " + toda.getValue() +
                "\nasda: " + asda.getValue() +
                "\nlda: " + lda.getValue() +
                "\ndisplaced threshold: " + displacedThreshold.getValue();
    }
}
