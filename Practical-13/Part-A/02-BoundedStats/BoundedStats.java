/**
 * Practical 13 - Part A - Program 2: Bounded stats.
 * One generic method with an upper bound, N extends Number, computes the
 * average of Integer[], Double[] and Long[] arrays.
 */
public class BoundedStats {

    /**
     * Average of any array of numbers. The bound N extends Number guarantees
     * that every element has doubleValue(); an empty array gives 0.0.
     */
    static <N extends Number> double average(N[] values) {
        if (values.length == 0) {
            return 0.0;
        }
        double sum = 0;
        for (N value : values) {
            sum += value.doubleValue();
        }
        return sum / values.length;
    }

    public static void main(String[] args) {
        Integer[] marks = {70, 85, 90, 64};
        Double[] temperatures = {31.5, 29.0, 33.25};
        Long[] balances = {5000L, 2000L, 9000L};
        Integer[] none = {};

        System.out.println("Average of Integer[] marks       = " + average(marks));
        System.out.println("Average of Double[] temperatures = " + average(temperatures));
        System.out.println("Average of Long[] balances       = " + average(balances));
        System.out.println("Average of an empty Integer[]    = " + average(none));

        // String[] names = {"a", "b"};
        // average(names);  <- does not compile: String is not a subtype of Number,
        //                     so it has no doubleValue() and violates the bound.
        System.out.println("Note: average(String[]) is rejected at compile time because "
                + "String does not extend Number.");
    }
}
