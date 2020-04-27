package runway.Model;

public class Obstacle {


    private String name;
    private double height;
    private ObstaclePositionParam oParam;

    public Obstacle(String name, double height) {
        this.name = name;
        this.height = height;

    }

    // Getter and setters for instance variables
    public String getName() {
        return name;
    }

    public double getHeight() {
        return height;
    }

    public ObstaclePositionParam getoParam() {
        return oParam;
    }

    public void setoParam(ObstaclePositionParam oParam) {
        this.oParam = oParam;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return name;
      /*  return "Obstacle{" +
                "name='" + name + '\'' +
                ", height=" + height +
                '}';*/
    }
}
