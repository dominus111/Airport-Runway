package runway.Model;

/**
 * Models a physical runway that has two virtual Runways, one for each direction
 */
public class Runway {

    public VirtualRunway leftRunway;
    public VirtualRunway rightRunway;

    public Runway(VirtualRunway leftRunway, VirtualRunway rightRunway) {
        this.leftRunway = leftRunway;
        this.rightRunway = rightRunway;
    }

  /*//  public String toString() {
        return "Runway " + leftRunway.toString() + "/" + ""
    }*/
}
