/**
 * Practical 05 - Part A - Program 3 (For Advanced Learners): Library late fees.
 * Each media type computes its own late fee from the number of days late.
 */
public abstract class Media {

    private final String title;
    private final int daysLate;

    protected Media(String title, int daysLate) {
        this.title = title;
        this.daysLate = Math.max(0, daysLate);
    }

    public String getTitle() {
        return title;
    }

    public int getDaysLate() {
        return daysLate;
    }

    /** Short description of this type's fee rule. */
    public abstract String feeRule();

    public abstract long lateFee();
}
