package runway.Model;

import java.util.concurrent.BlockingDeque;

public class Calculator {

    private final static int stripEnd = 60;
    private final static int distToCenterL = 75;
    private final static int RESA = 240;
    private final static int BLAST_ALLOWENCE = 300;

    public void calculate(ObstaclePositionParam oParam, Runway runway) {
        // Checks if further calculations are needed
        if(oParam.getDistToCenterL() > distToCenterL || (oParam.getDistToLTHR() < -stripEnd || oParam.getDistToRTHR() < -stripEnd))
            System.out.println("No further calculations are needed");
        else
            pickRecalculationCase(oParam, runway);
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

        double newTora, newLda;
        if ((stripEnd + RESA) > BLAST_ALLOWENCE) {
            newTora = runwayParam.getTora() - displacedTHR - distToTHR - (stripEnd + RESA);
        } else {
            newTora = runwayParam.getTora() - displacedTHR - distToTHR - BLAST_ALLOWENCE;
        }

        if(((50 * height + stripEnd) > BLAST_ALLOWENCE)) {
            newLda = runwayParam.getLda() - distToTHR - ((50 * height + stripEnd));
        } else {
            newLda = runwayParam.getLda() -distToTHR - BLAST_ALLOWENCE;
        }

        double newAsda = newTora + stopway;
        double newToda = newTora + clearway;

        runway.setRecalculatedParameters(new RunwayParameters(newTora,newToda,newAsda,newLda, 0));

    }


    public void  takeOffTowardsLandTowards(double displacedTHR, double height, double distToTHR, VirtualRunway runway) {
        double slopeValue = 50 * height;
        if (slopeValue < RESA)
            slopeValue = RESA;

        // When displaying check if fisplacedThr == 0
        double newTORA = distToTHR + displacedTHR - slopeValue - stripEnd;
        double newLDA = distToTHR - RESA - stripEnd;

        //TODO add displaced threshold
        runway.setRecalculatedParameters(new RunwayParameters(newTORA, newTORA, newTORA, newLDA, 0));
    }
}