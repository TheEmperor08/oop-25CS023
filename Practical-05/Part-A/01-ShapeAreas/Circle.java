/** Practical 05 - Part A - Program 1: a circle of a given radius. */
public class Circle extends Shape {

    private final double radius;

    public Circle(double radius) {
        super("Circle(r=" + radius + ")");
        this.radius = radius;
    }

    public double area() {
        return Math.PI * radius * radius;
    }
}
