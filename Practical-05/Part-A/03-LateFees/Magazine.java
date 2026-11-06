/** Practical 05 - Part A - Program 3: magazines have 3 free days, then a flat 30. */
public class Magazine extends Media {

    public Magazine(String title, int daysLate) {
        super(title, daysLate);
    }

    public String feeRule() {
        return "3 days grace, then flat 30";
    }

    public long lateFee() {
        return getDaysLate() > 3 ? 30 : 0;
    }
}
