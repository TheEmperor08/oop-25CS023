package exception;

/** Thrown when a withdrawal needs more money than the account may give. */
public class InsufficientFundsException extends BankException {

    private static final long serialVersionUID = 1L;

    /** How much more money would have been needed. */
    private final long shortfall;

    public InsufficientFundsException(String accountNumber, long shortfall) {
        super("Insufficient funds in " + accountNumber + ": short by " + shortfall);
        this.shortfall = shortfall;
    }

    public long getShortfall() {
        return shortfall;
    }
}
