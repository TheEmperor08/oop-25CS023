package model;

/**
 * Capability of an account that earns interest. Implementing classes supply the
 * rate and the balance; the default methods compute the interest for them.
 */
public interface InterestBearing {

    /** Yearly interest rate in percent. */
    double interestRate();

    long getBalance();

    /** Default method: one year's interest on the current balance. */
    default double yearlyInterest() {
        return round2(getBalance() * interestRate() / 100.0);
    }

    /** Supplementary second default method: balance after one year with interest. */
    default double projectedBalance() {
        return round2(getBalance() + yearlyInterest());
    }

    /** Static interface method: interest on any amount at any rate. */
    static double interestOn(long amount, double ratePercent) {
        return round2(amount * ratePercent / 100.0);
    }

    /** Private interface method: helper shared by the methods above. */
    private static double round2(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
