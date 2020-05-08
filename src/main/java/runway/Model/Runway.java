package runway.Model;

/**
 * Models a physical runway that has two virtual Runways, one for each direction
 */
public class Runway {

    private VirtualRunway leftRunway;
    private VirtualRunway rightRunway;
    private Obstacle obstacle = null;

    public Runway(VirtualRunway leftRunway, VirtualRunway rightRunway) {
        this.leftRunway = leftRunway;
        this.rightRunway = rightRunway;
        obstacle = null;
    }

    public String toString () {
        return "Runway " + leftRunway.toString() + "/" + rightRunway.toString();
    }

    public VirtualRunway getLeftRunway() {
        return leftRunway;
    }

    public VirtualRunway getRightRunway() {
        return rightRunway;
    }

    public void setObstacle(Obstacle obstacle){
        this.obstacle = obstacle;
    }

    public Obstacle getObstacle(){
        return obstacle;
    }

    public String getRunway() {
        return "Runway{" +
                "leftRunway=" + leftRunway.getVirtualRunway() +
                ", rightRunway=" + rightRunway.getVirtualRunway() +
                '}';
    }

    public boolean isSame(Runway obj) {
        if (!leftRunway.isSame(obj.leftRunway)) return false;
        if (!rightRunway.isSame(obj.rightRunway)) return false;
        return true;
    }
}
