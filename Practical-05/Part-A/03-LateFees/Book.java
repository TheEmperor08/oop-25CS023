/** Practical 05 - Part A - Program 3: books cost 5 per day late, capped at 100. */
public class Book extends Media {

    public Book(String title, int daysLate) {
        super(title, daysLate);
    }

    public String feeRule() {
        return "5/day, max 100";
    }

    public long lateFee() {
        return Math.min(100, 5L * getDaysLate());
    }
}
