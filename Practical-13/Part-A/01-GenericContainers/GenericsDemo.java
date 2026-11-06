/**
 * Practical 13 - Part A - Program 1: Generic containers.
 * Uses Box and Pair with different types to show type safety without casts,
 * and a generic method that returns the first element of any array.
 */
public class GenericsDemo {

    /** Returns the first element of any array, or null when the array is empty. */
    static <T> T first(T[] items) {
        return items.length == 0 ? null : items[0];
    }

    public static void main(String[] args) {
        Box<Integer> numberBox = new Box<>(42);
        Box<String> textBox = new Box<>("hello");
        int doubled = numberBox.get() * 2;          // no cast needed: get() returns Integer
        String upper = textBox.get().toUpperCase(); // no cast needed: get() returns String
        System.out.println(numberBox + " doubled = " + doubled);
        System.out.println(textBox + " upper-case = " + upper);
        // numberBox.set("forty-two");   <- compile-time error: String is not an Integer

        textBox.set("generics");
        System.out.println("After set: " + textBox);

        Pair<String, Integer> student = new Pair<>("Riya", 101);
        Pair<String, Double> price = new Pair<>("Pen", 12.5);
        System.out.println("Student pair " + student + " -> name length "
                + student.getFirst().length() + ", roll + 1 = " + (student.getSecond() + 1));
        System.out.println("Price pair " + price + " -> with GST "
                + (price.getSecond() * 1.18));

        Integer[] marks = {78, 91, 66};
        String[] cities = {"Anand", "Changa", "Nadiad"};
        Double[] empty = {};
        Integer firstMark = first(marks);
        String firstCity = first(cities);
        System.out.println("first(marks) = " + firstMark);
        System.out.println("first(cities) = " + firstCity);
        System.out.println("first(empty) = " + first(empty));
    }
}
