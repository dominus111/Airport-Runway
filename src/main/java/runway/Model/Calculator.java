package runway.Model;

import java.util.concurrent.BlockingDeque;

public class Calculator {

    private final static int stripEnd = 60;
    private final static int distToCenterL = 75;
    private final static int RESA = 240;
    private final static int BLAST_ALLOWENCE = 300;

    public void calculate(ObstaclePositionParam oParam, Runway runway) {
        // Checks if further calculations are needed
        if(oParam.getDistToCenterL() > distToCenterL || (oParam.getDistToLTHR() < -stripEnd || oParam.getDistToRTHR() < -stripEnd)) {
            runway.getRightRunway().setRecalculatedParameters(runway.getRightRunway().getInitialParameters());
            runway.getLeftRunway().setRecalculatedParameters((runway.getLeftRunway().getInitialParameters()));
            runway.getLeftRunway().getRecalculatedParameters().setCalculationBrkdwn("No further calculations are needed. \n The recalculated values are the same as the initial runway parameters.");
            runway.getRightRunway().getRecalculatedParameters().setCalculationBrkdwn("No further calculations are needed. \n The recalculated values are the same as the initial runway parameters.");
        } else
            pickRecalculationCase(oParam, runway);
        }

    public void pickRecalculationCase (ObstaclePositionParam oParam, Runway runway) {

        double lDisplacemnet = runway.getLeftRunway().getInitialParameters().getdispTHR();
        double rDisplacement = runway.getRightRunway().getInitialParameters().getdispTHR();
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

    public void  takeOffAwayLandOver(double height, double distToTHR, VirtualRunway runway) {
        // Initial necessary values
        RunwayParameters runwayParam = runway.getInitialParameters();
        double stopway = runwayParam.getAsda() - runwayParam.getTora();
        double clearway = runwayParam.getToda() - runwayParam.getTora();
        double displacedTHR = runwayParam.getdispTHR();
        String calculationBrkdwn = "";

        double newTora, newLda;
        if ((stripEnd + RESA) > BLAST_ALLOWENCE) {
            newTora = runwayParam.getTora() - displacedTHR - distToTHR - (stripEnd + RESA);
            calculationBrkdwn+="TORA = Initial TORA - Displaced Threshold - Distance from Threshold - Strip End - RESA\n" +
                    "TORA = "+runwayParam.getTora()+" - "+displacedTHR+" - "+distToTHR+" - "+ (stripEnd + RESA)+"\n" +
                    "TORA = "+newTora+"\n \n";

        } else {
            newTora = runwayParam.getTora() - displacedTHR - distToTHR - BLAST_ALLOWENCE;
            calculationBrkdwn+="TORA = Initial TORA - Displaced Threshold - Distance from Threshold  - Blast Protection\n" +
                    "TORA = "+runwayParam.getTora()+" - "+displacedTHR+" - "+distToTHR+" - "+ BLAST_ALLOWENCE +"\n" +
                    "TORA = "+newTora+ "\n \n";
        }

        if(((50 * height + stripEnd) > BLAST_ALLOWENCE)) {
            newLda = runwayParam.getLda() - distToTHR - ((50 * height + stripEnd));
            calculationBrkdwn+="LDA = Initial LDA - Distance from Threshold - Slope Angle * Obstacle Height + Strip End\n"+
                    "LDA = "+runwayParam.getLda()+" - "+distToTHR+" - "+((50 * height))+" + "+stripEnd +"\n" +
                    "LDA = "+newLda+ "\n \n";

        } else {
            newLda = runwayParam.getLda() -distToTHR - BLAST_ALLOWENCE;
            calculationBrkdwn+="LDA = Initial LDA - Distance from Threshold - Blast Protection\n"+
                    "LDA = "+runwayParam.getLda()+" - "+distToTHR+" - "+BLAST_ALLOWENCE +
                    "LDA = "+newLda+ "\n \n";
        }

        double newAsda = newTora + stopway;
        double newToda = newTora + clearway;
        calculationBrkdwn+="ASDA = TORA + stopway\n"+"ASDA = "+newTora+" + "+stopway+"\n" +"ASDA = "+newAsda+"\n \n";
        calculationBrkdwn+="TODA = TORA + clearway\n"+"TODA = "+newTora+" + "+clearway+"\n"+ "TODA = "+newToda+"\n \n";

        runway.setRecalculatedParameters(new RunwayParameters(newTora,newToda,newAsda,newLda, 0));
        runway.getRecalculatedParameters().setCalculationBrkdwn(calculationBrkdwn);
    }


    public void  takeOffTowardsLandTowards(double displacedTHR, double height, double distToTHR, VirtualRunway runway) {

        String calculationBrkdwn = "";
        double slopeValue = 50 * height;
        if (slopeValue < RESA)
            slopeValue = RESA;
        double newTORA = distToTHR + displacedTHR - slopeValue - stripEnd;
        double newLDA = distToTHR - RESA - stripEnd;

        // When displaying check if fisplacedThr == 0
        if(displacedTHR == 0)
            calculationBrkdwn += "TORA = Distance from Threshold - Slope Calculation - Strip End \n" +
                    "TORA = " + distToTHR +" - "+slopeValue+" - "+stripEnd+"\n" +
                    "TORA = " + newTORA + "\n \n";

        else
            calculationBrkdwn += "TORA = Distance from Threshold + Displaced Threshold - Slope Calculation - Strip End\n" +
                    "TORA = "+distToTHR+" + "+displacedTHR+" - "+slopeValue+" - "+stripEnd+"\n" +
                    "TORA = " +newTORA+ "\n \n";
        calculationBrkdwn += "TODA = Redeclared TORA\n"+"TODA = "+newTORA+"\n \n";
        calculationBrkdwn += "ASDA = Redeclared TORA\n"+"ASDA = "+newTORA+"\n \n";
        calculationBrkdwn += "LDA = Distance from Threshold - Strip End - RESA\n"+"LDA = "+distToTHR+" - "+stripEnd+" - "+RESA+"\n" + "LDA = " +newLDA+ "\n \n";

        //TODO add displaced threshold
        runway.setRecalculatedParameters(new RunwayParameters(newTORA, newTORA, newTORA, newLDA, 0));
        runway.getRecalculatedParameters().setCalculationBrkdwn(calculationBrkdwn);
    }
}
