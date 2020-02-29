package runway.Model;

/**
 * Represents one of the runway's directions.
 * Contains the old and the new calculations of the distances.
 */
public class VirtualRunway {

    public String designator;
    private RunwayParameters initialParameters;
    private RunwayParameters recalculatedParameters;

    public VirtualRunway(String designator) {
        this.designator = designator;
    }

    @Override
    public String toString() {
        return designator;
    }

    public RunwayParameters getInitialParameters() {
        return initialParameters;
    }

    public void setRecalculatedParameters(RunwayParameters recalculatedParameters) {
        this.recalculatedParameters = recalculatedParameters;
    }
}
