/**
 * Practical 03 - Part A - Program 1: Distinct points (driver).
 * Counts the distinct points in an array using equals().
 */
public class Driver {

    public static void main(String[] args) {
        Point[] points = {
            new Point(1, 2), new Point(3, 4), new Point(1, 2),
            new Point(5, 6), new Point(3, 4)
        };

        int distinct = 0;
        for (int i = 0; i < points.length; i++) {
            boolean seenBefore = false;
            for (int j = 0; j < i; j++) {
                if (points[i].equals(points[j])) {
                    seenBefore = true;
                    break;
                }
            }
            System.out.println(points[i] + (seenBefore ? "  repeat" : "  new"));
            if (!seenBefore) {
                distinct++;
            }
        }
        System.out.println("Distinct: " + distinct);
        System.out.println("hashCode equal for (1, 2) twice: "
                + (points[0].hashCode() == points[2].hashCode()));
    }
}
