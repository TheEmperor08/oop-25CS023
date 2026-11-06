/** Practical 05 - Part A - Program 3: DVDs cost 20 per day late, no cap. */
public class Dvd extends Media {

    public Dvd(String title, int daysLate) {
        super(title, daysLate);
    }

    public String feeRule() {
        return "20/day, no cap";
    }

    public long lateFee() {
        return 20L * getDaysLate();
    }
}
