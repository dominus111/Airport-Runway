package runway.Model;

public class Obstacle {

    private String name;
    private double height, length;

    public Obstacle(String name, double height, double length) {
        this.name = name;
        this.height = height;
        this.length = length;
    }

    // Getter and setters for instance variables
    public String getName() {
        return name;
    }

    public double getHeight() {
        return height;
    }

    public double getLength() {
        return length;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setLength(double length) {
        this.length = length;
    }
}
