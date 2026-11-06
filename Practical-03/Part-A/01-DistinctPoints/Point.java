import java.util.Objects;

/**
 * Practical 03 - Part A - Program 1: Distinct points.
 * A 2-D point compared by value: equals() and hashCode() use x and y.
 */
public class Point {

    private int x;
    private int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Point)) {
            return false;
        }
        Point other = (Point) o;
        return x == other.x && y == other.y;
    }

    public int hashCode() {
        return Objects.hash(x, y);
    }
}
