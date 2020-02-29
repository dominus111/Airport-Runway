package runway.Model;

public class RunwayParameters {

    private double tora, toda, asda, lda;

    public RunwayParameters(double tora, double toda, double asda, double lda) {
        this.tora = tora;
        this.toda = toda;
        this.asda = asda;
        this.lda = lda;
    }

    public double getLda() {
        return lda;
    }

    public void setLda(double lda) {
        this.lda = lda;
    }

    public double getAsda() {
        return asda;
    }

    public void setAsda(double asda) {
        this.asda = asda;
    }

    public double getToda() {
        return toda;
    }

    public void setToda(double toda) {
        this.toda = toda;
    }

    public double getTora() {
        return tora;
    }

    public void setTora(double tora) {
        this.tora = tora;
    }
}
