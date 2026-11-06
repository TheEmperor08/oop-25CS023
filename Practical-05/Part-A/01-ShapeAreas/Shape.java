/**
 * Practical 05 - Part A - Program 1: Shape areas.
 * Abstract base class: every shape must say how to compute its area.
 */
public abstract class Shape {

    private final String name;

    protected Shape(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract double area();
}
