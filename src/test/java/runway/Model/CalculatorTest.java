package runway.Model;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

    @DisplayName("Heathrow 1")
    @Tag("fast")
    @Test
    void heathrow1() {
        Runway runway09L27R  = new Runway(new VirtualRunway("09L", new RunwayParameters(3902,3902,3902,3595, 306)), new VirtualRunway("27R", new RunwayParameters(3884,3962,3884,3884, 0)));

        Obstacle obstacle = new Obstacle("mock",12);
        ObstaclePositionParam oParam = new ObstaclePositionParam(obstacle, -50,3646,0);
        Calculator calculator = new Calculator();

        calculator.calculate(oParam, runway09L27R);
        System.out.println("Data for Heathrow Scenario 1\n");

        var leftParams = runway09L27R.getLeftRunway().getRecalculatedParameters();

        System.out.println("Data for left runway");
        System.out.println(leftParams);
        assertEquals(3346, leftParams.getTora());
        assertEquals(3346, leftParams.getAsda());
        assertEquals(3346, leftParams.getToda());
        assertEquals(2985, leftParams.getLda());

        var rightParams = runway09L27R.getRightRunway().getRecalculatedParameters();

        System.out.println("Data for right runway");
        System.out.println(rightParams);
        assertEquals(2986, rightParams.getToda());
        assertEquals(2986, rightParams.getAsda());
        assertEquals(2986, rightParams.getToda());
        assertEquals(3346, rightParams.getLda());
    }

    @DisplayName("Heathrow 2")
    @Tag("fast")
    @Test
    void heathrow2() {
        Runway runway09R27L  = new Runway(new VirtualRunway("09R", new RunwayParameters(3660,3660,3660,3353, 307)), new VirtualRunway("27L", new RunwayParameters(3660,3660,3660,3660, 0)));

        Obstacle obstacle = new Obstacle("mock",25);
        ObstaclePositionParam oParam = new ObstaclePositionParam(obstacle, 2853,500,20);
        Calculator calculator = new Calculator();

        calculator.calculate(oParam, runway09R27L);
        System.out.println("Data for Heathrow Scenario 2\n");

        var leftParams = runway09R27L.getLeftRunway().getRecalculatedParameters();

        System.out.println("Data for left runway");
        System.out.println(leftParams);
        assertEquals(1850, leftParams.getTora());
        assertEquals(1850, leftParams.getAsda());
        assertEquals(1850, leftParams.getToda());
        assertEquals(2553, leftParams.getLda());

        var rightParams = runway09R27L.getRightRunway().getRecalculatedParameters();

        System.out.println("Data for right runway");
        System.out.println(rightParams);
        assertEquals(2860, rightParams.getToda());
        assertEquals(2860, rightParams.getAsda());
        assertEquals(2860, rightParams.getToda());
        assertEquals(1850, rightParams.getLda());
    }

    @DisplayName("Heathrow 3")
    @Tag("fast")
    @Test
    void heathrow3() {
        Runway runway09R27L  = new Runway(new VirtualRunway("09R", new RunwayParameters(3660,3660,3660,3353, 307)), new VirtualRunway("27L", new RunwayParameters(3660,3660,3660,3660, 0)));

        Obstacle obstacle = new Obstacle("mock",15);
        ObstaclePositionParam oParam = new ObstaclePositionParam(obstacle, 150,3203,60);
        Calculator calculator = new Calculator();

        calculator.calculate(oParam, runway09R27L);
        System.out.println("Data for Heathrow Scenario 3\n");

        var leftParams = runway09R27L.getLeftRunway().getRecalculatedParameters();

        System.out.println("Data for left runway");
        System.out.println(leftParams);
        assertEquals(2903, leftParams.getTora());
        assertEquals(2903, leftParams.getAsda());
        assertEquals(2903, leftParams.getToda());
        assertEquals(2393, leftParams.getLda());

        var rightParams = runway09R27L.getRightRunway().getRecalculatedParameters();

        System.out.println("Data for right runway");
        System.out.println(rightParams);
        assertEquals(2393, rightParams.getToda());
        assertEquals(2393, rightParams.getAsda());
        assertEquals(2393, rightParams.getToda());
        assertEquals(2903, rightParams.getLda());
    }

    //@DisplayName("Heathrow 4")
    //@Tag("fast")
    //@Test
    void heathrow4() {
        Runway runway09L27R  = new Runway(new VirtualRunway("09L", new RunwayParameters(3902,3902,3902,3595, 306)), new VirtualRunway("27R", new RunwayParameters(3884,3962,3884,3884, 0)));

        Obstacle obstacle = new Obstacle("mock",20);
        ObstaclePositionParam oParam = new ObstaclePositionParam(obstacle, 3546,50,-20);
        Calculator calculator = new Calculator();

        calculator.calculate(oParam, runway09L27R);
        System.out.println("Data for Heathrow Scenario 4");

        var leftParams = runway09L27R.getLeftRunway().getRecalculatedParameters();

        System.out.println("Data for left runway");
        System.out.println(leftParams);
        assertEquals(2792, leftParams.getTora());
        assertEquals(2792, leftParams.getAsda());
        assertEquals(2792, leftParams.getToda());
        assertEquals(3246, leftParams.getLda());

        var rightParams = runway09L27R.getRightRunway().getRecalculatedParameters();

        System.out.println("Data for right runway");
        System.out.println(rightParams);
        assertEquals(3534, rightParams.getToda());
        assertEquals(3534, rightParams.getAsda());
        assertEquals(3612, rightParams.getToda());
        assertEquals(2774, rightParams.getLda());
    }

}