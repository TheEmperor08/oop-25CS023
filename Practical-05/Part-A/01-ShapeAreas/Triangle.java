/** Practical 05 - Part A - Program 1: a triangle of a given base and height. */
public class Triangle extends Shape {

    private final double base;
    private final double height;

    public Triangle(double base, double height) {
        super("Triangle(b=" + base + ", h=" + height + ")");
        this.base = base;
        this.height = height;
    }

    public double area() {
        return 0.5 * base * height;
    }
}
