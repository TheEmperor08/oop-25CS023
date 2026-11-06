/** Practical 05 - Part A - Program 1: a rectangle of a given width and height. */
public class Rectangle extends Shape {

    private final double width;
    private final double height;

    public Rectangle(double width, double height) {
        super("Rectangle(" + width + " x " + height + ")");
        this.width = width;
        this.height = height;
    }

    public double area() {
        return width * height;
    }
}
