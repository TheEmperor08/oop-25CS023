package exception;

/** Supplementary: thrown when a withdrawal would exceed the daily cap. */
public class DailyLimitExceededException extends BankException {

    private static final long serialVersionUID = 1L;

    public DailyLimitExceededException(long dailyLimit, long alreadyWithdrawn, long requested) {
        super("Daily withdrawal limit " + dailyLimit + " exceeded: already withdrawn "
                + alreadyWithdrawn + ", requested " + requested);
    }
}
