package runway.Model;

/**
 * Represents one of the runway's directions.
 * Contains the old and the new calculations of the distances.
 */
public class VirtualRunway {

    public String direction;

    public VirtualRunway(String direction) {
        this.direction = direction;
    }

    @Override
    public String toString() {
        return direction;
    }
}
