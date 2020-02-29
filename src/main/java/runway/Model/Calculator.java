package runway.Model;

public class Calculator {

    // Blast allowence = RESA + stripEnc
    private final static int stripEnd = 60;
    private final static int distToCenterL = 75;
    private final static int RESA = 240;

    public void calculate(ObstaclePositionParam oParam, Runway runway) {
        // Checks if further calculations are needed
        if(oParam.getDistToCenterL() < distToCenterL && (oParam.getDistToLTHR() > -stripEnd || oParam.getDistToRTHR() > -stripEnd))
            pickRecalculationCase(oParam, runway);
        else
            System.out.println("No further calculations are needed");
    }

    public void pickRecalculationCase (ObstaclePositionParam oParam, Runway runway) {
        double rDisplacement = runway.getRightRunway().getInitialParameters().getTora() - runway.getRightRunway().getInitialParameters().getLda();
        double lDisplacemnet = runway.getLeftRunway().getInitialParameters().getTora() - runway.getLeftRunway().getInitialParameters().getLda();

        double objHeight = oParam.getObstacle().getHeight();

        // Decide which runway is used for takes off/landing towards/away from the obstacle
        if (oParam.getDistToLTHR() < oParam.getDistToRTHR()) {
            takeOffAwayLandOver(objHeight, oParam.getDistToLTHR(), runway.getLeftRunway());
            takeOffTowardsLandTowards(rDisplacement, objHeight, oParam.getDistToRTHR(), runway.getRightRunway());
        } else {
            takeOffAwayLandOver(objHeight, oParam.getDistToRTHR(), runway.getRightRunway());
            takeOffTowardsLandTowards(lDisplacemnet, objHeight, oParam.getDistToLTHR(), runway.getLeftRunway());
        }
    }

    public void  takeOffAwayLandOver(double height, double distToTHR, VirtualRunway runway ) {
        // Initial necessary values
        RunwayParameters runwayParam = runway.getInitialParameters();
        double stopway = runwayParam.getAsda() - runwayParam.getTora();
        double clearway = runwayParam.getToda() - runwayParam.getTora();
        double displacedTHR = runwayParam.getTora() - runwayParam.getLda();

        // Recalculated values
        // RESA + strip end = blastProtection so it won't be taken into consideration
        double newTora = runwayParam.getTora() - RESA - stripEnd - distToTHR - displacedTHR;
        double newToda = newTora + clearway;
        double newAsda = newTora + stopway;

        double slopeValue = 50 * height;
        if(slopeValue + distToTHR < RESA)
            slopeValue = RESA;
        double newLda = runwayParam.getLda() - slopeValue - distToTHR - stripEnd;

        runway.setRecalculatedParameters(new RunwayParameters(newTora,newToda,newAsda,newLda));
    }


    public void  takeOffTowardsLandTowards(double displacedTHR, double height, double distToTHR, VirtualRunway runway){
        double slopeValue = 50 * height;
        if(slopeValue < RESA)
            slopeValue = RESA;

        double newTORA = distToTHR + displacedTHR - slopeValue - stripEnd;
        double newLDA = distToTHR - RESA - stripEnd;

        runway.setRecalculatedParameters(new RunwayParameters(newTORA,newTORA,newTORA, newLDA));

    }

}
