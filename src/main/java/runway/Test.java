package runway;

import runway.Model.*;

public class Test {

    private Runway runway09R27L  = new Runway(new VirtualRunway("09R", new RunwayParameters(3660,3660,3660,3353, 307)), new VirtualRunway("27L", new RunwayParameters(3660,3660,3660,3660, 0)));
    private Runway runway09L27R  = new Runway(new VirtualRunway("09L", new RunwayParameters(3902,3902,3902,3595, 306)), new VirtualRunway("27R", new RunwayParameters(3884,3962,3884,3884, 0)));

    public Test() {
        heathRow1();
        heathRow2();
        heathRow3();
    }

    public void heathRow1 () {
        Obstacle obstacle = new Obstacle("mock",12);
        ObstaclePositionParam oParam = new ObstaclePositionParam(obstacle, -50,3646,0);
        Calculator calculator = new Calculator();

        calculator.calculate(oParam, runway09L27R);
        System.out.println("Data for Heathrow Scenario 1");

        System.out.println("Data for left runway");
        if(runway09L27R.getLeftRunway().getRecalculatedParameters().getTora() == 3346 )
            System.out.println("Recalculated TORA is ok.");
        if(runway09L27R.getLeftRunway().getRecalculatedParameters().getAsda() == 3346 )
            System.out.println("Recalculated ASDA is ok.");
        if(runway09L27R.getLeftRunway().getRecalculatedParameters().getToda() == 3346 )
            System.out.println("Recalculated TODA is ok.");
        if(runway09L27R.getLeftRunway().getRecalculatedParameters().getLda() == 2985 )
            System.out.println("Recalculated LDA is ok.");


        System.out.println("Data for right runway");
        if(runway09L27R.getRightRunway().getRecalculatedParameters().getTora() == 2986 )
            System.out.println("Recalculated TORA is ok.");
        if(runway09L27R.getRightRunway().getRecalculatedParameters().getAsda() == 2986 )
            System.out.println("Recalculated ASDA is ok.");
        if(runway09L27R.getRightRunway().getRecalculatedParameters().getToda() == 2986 )
            System.out.println("Recalculated TODA is ok.");
        if(runway09L27R.getRightRunway().getRecalculatedParameters().getLda() ==  3346 )
            System.out.println("Recalculated LDA is ok.");

    }

    public void heathRow2 () {
        Obstacle obstacle = new Obstacle("mock",25 );
        ObstaclePositionParam oParam = new ObstaclePositionParam(obstacle, 2853,500,20);
        Calculator calculator = new Calculator();

        calculator.calculate(oParam, runway09R27L);
        System.out.println("Data for Heathrow Scenario 2");

        System.out.println("Data for left runway");
        if(runway09R27L.getLeftRunway().getRecalculatedParameters().getTora() == 1850 )
            System.out.println("Recalculated TORA is ok.");
        if(runway09R27L.getLeftRunway().getRecalculatedParameters().getAsda() == 1850 )
            System.out.println("Recalculated ASDA is ok.");
        if(runway09R27L.getLeftRunway().getRecalculatedParameters().getToda() == 1850 )
            System.out.println("Recalculated TODA is ok.");
        if(runway09R27L.getLeftRunway().getRecalculatedParameters().getLda() == 2553 )
            System.out.println("Recalculated LDA is ok.");


        System.out.println("Data for right runway");
        if(runway09R27L.getRightRunway().getRecalculatedParameters().getTora() == 2860 )
            System.out.println("Recalculated TORA is ok.");
        if(runway09R27L.getRightRunway().getRecalculatedParameters().getAsda() == 2860 )
            System.out.println("Recalculated ASDA is ok.");
        if(runway09R27L.getRightRunway().getRecalculatedParameters().getToda() == 2860 )
            System.out.println("Recalculated TODA is ok.");
        if(runway09R27L.getRightRunway().getRecalculatedParameters().getLda() ==  1850 )
            System.out.println("Recalculated LDA is ok.");

    }


    public void heathRow3 () {
        Obstacle obstacle = new Obstacle("mock",15);
        ObstaclePositionParam oParam = new ObstaclePositionParam(obstacle, 150,3203,60);
        Calculator calculator = new Calculator();

        calculator.calculate(oParam, runway09R27L);
        System.out.println("Data for Heathrow Scenario 3");
        System.out.println("Data for left runway");
        if(runway09R27L.getLeftRunway().getRecalculatedParameters().getTora() == 2903 )
            System.out.println("Recalculated TORA is ok.");
        if(runway09R27L.getLeftRunway().getRecalculatedParameters().getAsda() == 2903 )
            System.out.println("Recalculated ASDA is ok.");
        if(runway09R27L.getLeftRunway().getRecalculatedParameters().getToda() == 2903 )
            System.out.println("Recalculated TODA is ok.");
        if(runway09R27L.getLeftRunway().getRecalculatedParameters().getLda() == 2393 )
            System.out.println("Recalculated LDA is ok.");

        System.out.println("Data for right runway");
        if(runway09R27L.getRightRunway().getRecalculatedParameters().getTora() == 2393 )
            System.out.println("Recalculated TORA is ok.");
        if(runway09R27L.getRightRunway().getRecalculatedParameters().getAsda() == 2393 )
            System.out.println("Recalculated ASDA is ok.");
        if(runway09R27L.getRightRunway().getRecalculatedParameters().getToda() == 2393 )
            System.out.println("Recalculated TODA is ok.");
        if(runway09R27L.getRightRunway().getRecalculatedParameters().getLda() ==  2903 )
            System.out.println("Recalculated LDA is ok.");

    }

}
