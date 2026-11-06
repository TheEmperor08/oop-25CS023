/**
 * Practical 05 - Part A - Program 3 (For Advanced Learners): Library returns (main).
 * Totals the late fees for a returned batch using polymorphism.
 */
public class LibraryReturns {

    public static void main(String[] args) {
        Media[] batch = {
            new Book("Clean Code", 4),
            new Book("Effective Java", 30),
            new Dvd("Interstellar", 3),
            new Magazine("Tech Today", 2),
            new Magazine("Science Weekly", 6)
        };

        long total = 0;
        for (Media m : batch) {
            long fee = m.lateFee();
            total += fee;
            System.out.printf("%-9s %-15s %2d days late  [%-26s]  fee %4d%n",
                    m.getClass().getSimpleName(), m.getTitle(), m.getDaysLate(),
                    m.feeRule(), fee);
        }
        System.out.println("Total late fees for this batch: " + total);
    }
}
