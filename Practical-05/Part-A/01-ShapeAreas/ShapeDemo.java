/**
 * Practical 05 - Part A - Program 1: Shape areas (main).
 * One loop handles every shape through polymorphism: area() is chosen
 * at run time from the object's real class.
 */
public class ShapeDemo {

    public static void main(String[] args) {
        Shape[] shapes = {
            new Circle(2), new Rectangle(4, 5), new Triangle(6, 3), new Circle(3)
        };

        double total = 0;
        Shape largest = null;
        for (Shape s : shapes) {
            double area = s.area();
            total += area;
            if (largest == null || area > largest.area()) {
                largest = s;
            }
            System.out.printf("%-26s area = %7.2f   running total = %7.2f%n",
                    s.getName(), area, total);
        }
        System.out.printf("Total area: %.2f%n", total);
        System.out.printf("Largest: %s with area %.2f%n", largest.getName(), largest.area());
    }
}
