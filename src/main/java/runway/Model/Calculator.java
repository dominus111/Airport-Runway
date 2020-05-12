package runway.Model;

import java.util.concurrent.BlockingDeque;

public class Calculator {

    private final static int stripEnd = 60;
    private final static int distToCenterL = 75;
    private final static int RESA = 240;
    private final static int BLAST_ALLOWENCE = 300;
    boolean landing = false;

    public void setLanding(boolean l){
        this.landing = l;
    }

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
        if ( runway.getLeftRunway().getInitialParameters().getTora() / 2 > oParam.getDistToLTHR()) {
            takeOffAwayLandOver(objHeight, oParam.getDistToLTHR(), runway.getLeftRunway(),landing);
        } else {
            takeOffTowardsLandTowards(objHeight, oParam.getDistToLTHR(), runway.getLeftRunway(), landing);
        }
        if ( runway.getRightRunway().getInitialParameters().getTora() / 2 > oParam.getDistToRTHR()) {
            takeOffAwayLandOver(objHeight, oParam.getDistToRTHR(), runway.getRightRunway(), landing);
        } else {
            takeOffTowardsLandTowards(objHeight, oParam.getDistToRTHR(), runway.getRightRunway(), landing);
        }

    }



    public void  takeOffAwayLandOver(double height, double distToTHR, VirtualRunway runway, boolean land) {
        // Initial necessary values
        RunwayParameters runwayParam = runway.getInitialParameters();
        double stopway = runwayParam.getAsda() - runwayParam.getTora();
        double clearway = runwayParam.getToda() - runwayParam.getTora();
        //double displacedTHR = runwayParam.getdispTHR();
        String calculationBrkdwn = "";

        double newTora = 0, newLda = 0, newToda = 0, newAsda = 0;
        if (land) {
            newLda = runwayParam.getTora() - distToTHR - (stripEnd + RESA) - (50*height);
            calculationBrkdwn+="LDA = Tora - Object treshold - Strip end - RESA - height*slope\n" +
                    "LDA = "+runwayParam.getLda();

        } else {
            newTora = runwayParam.getTora() - distToTHR - (stripEnd + RESA);
            newAsda = runwayParam.getAsda() - distToTHR - (stripEnd + RESA);
            newToda = runwayParam.getToda() - distToTHR - (stripEnd + RESA);
            calculationBrkdwn+="TORA = Initial TORA - Object Threshold - Strip End - RESA\n" +
                    "TORA = "+runwayParam.getTora()+" - "+distToTHR+" - "+ (stripEnd + RESA)+"\n" +
                    "TORA = "+newTora+"\n \n" +
            "ASDA = " + newAsda+ "\n \n" +
            "TODA = "+newToda+ " \n \n";
        }
        /*
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

        //calculationBrkdwn+="ASDA = TORA + stopway\n"+"ASDA = "+newTora+" + "+stopway+"\n" +"ASDA = "+newAsda+"\n \n";
        //calculationBrkdwn+="TODA = TORA + clearway\n"+"TODA = "+newTora+" + "+clearway+"\n"+ "TODA = "+newToda+"\n \n";
        */
        runway.setRecalculatedParameters(new RunwayParameters(newTora,newToda,newAsda,newLda, 0));
        runway.getRecalculatedParameters().setCalculationBrkdwn(calculationBrkdwn);
    }


    public void  takeOffTowardsLandTowards( double height, double distToTHR, VirtualRunway runway, boolean land) {

        RunwayParameters runwayParam = runway.getInitialParameters();
        String calculationBrkdwn = "";
        double distT = runwayParam.getTora()- distToTHR;
        double slopeValue = 50 * height;
        if (slopeValue < RESA)
            slopeValue = RESA;
        double newTora = 0, newLda = 0, newToda = 0, newAsda = 0;

        if (land) {
            newLda = runwayParam.getTora() - distT - (stripEnd + RESA);
            calculationBrkdwn+="LDA = Tora - Object treshold - Strip end - RESA\n" +
                    "LDA = "+runwayParam.getLda();

        } else {
            newTora = runwayParam.getTora() - distT - (stripEnd + RESA)- (50*height);
            newAsda = runwayParam.getAsda() - distT - (stripEnd + RESA)- (50*height);
            newToda = newTora;
            calculationBrkdwn+="TORA = Initial TORA - Object Threshold - Strip End - RESA - height*slope\n" +
                    "TORA = "+runwayParam.getTora()+" - "+distToTHR+" - "+ (stripEnd + RESA)+" - "+ slopeValue+ "\n" +
                    "TORA = "+newTora+"\n \n" +
                    "ASDA = "  + newAsda+ "\n \n" +
                    "TODA = " + newToda+ " \n \n";
        }

        // When displaying check if fisplacedThr == 0
        /*
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
        */
        //TODO add displaced threshold
        runway.setRecalculatedParameters(new RunwayParameters(newTora,newToda,newAsda,newLda, 0));
        runway.getRecalculatedParameters().setCalculationBrkdwn(calculationBrkdwn);
    }
}
