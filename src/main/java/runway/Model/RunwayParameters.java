package runway.Model;

public class RunwayParameters {

    public RunwayParameters(int tora, int toda, int asda, int lda) {
        this.tora = tora;
        this.toda = toda;
        this.asda = asda;
        this.lda = lda;
    }

    private int tora, toda, asda, lda;

    public int getLda() {
        return lda;
    }

    public void setLda(int lda) {
        this.lda = lda;
    }

    public int getAsda() {
        return asda;
    }

    public void setAsda(int asda) {
        this.asda = asda;
    }

    public int getToda() {
        return toda;
    }

    public void setToda(int toda) {
        this.toda = toda;
    }

    public int getTora() {
        return tora;
    }

    public void setTora(int tora) {
        this.tora = tora;
    }
}
