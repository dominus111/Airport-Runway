package runway.Model;

/**
 * Represents one of the runway's directions.
 * Contains the old and the new calculations of the distances.
 */
public class VirtualRunway {

    public String designator;
    private RunwayParameters initialParameters;
    private RunwayParameters recalculatedParameters;

    public VirtualRunway(String designator, RunwayParameters initialParameters) {
        this.designator = designator;
        this.initialParameters = initialParameters;
    }

    @Override
    public String toString() {
        return designator;
    }

    public RunwayParameters getInitialParameters() {
        return initialParameters;
    }

    public String getDesignator() {return  designator; }

    public void setRecalculatedParameters(RunwayParameters recalculatedParameters) {
        this.recalculatedParameters = recalculatedParameters;
    }

    public RunwayParameters getRecalculatedParameters() {
        return recalculatedParameters;
    }
}
