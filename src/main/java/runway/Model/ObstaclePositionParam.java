package runway.Model;

public class ObstaclePositionParam {

    private Obstacle obstacle;
    private double distToLTHR, distToRTHR, distToCenterL;
    private VirtualRunway virtualRunway;

    public ObstaclePositionParam(Obstacle obstacle, double distToLTHR, double distToRTHR, double distToCenterL, VirtualRunway virtualRunway) {
        this.obstacle = obstacle;
        this.distToLTHR = distToLTHR;
        this.distToRTHR = distToRTHR;
        this.distToCenterL = distToCenterL;
        this.virtualRunway = virtualRunway;
    }

    public Obstacle getObstacle() {
        return obstacle;
    }

    public double getDistToLTHR() {
        return distToLTHR;
    }

    public double getDistToRTHR() {
        return distToRTHR;
    }

    public double getDistToCenterL() {
        return distToCenterL;
    }

    public VirtualRunway getVirtualRunway() {
        return virtualRunway;
    }
}
