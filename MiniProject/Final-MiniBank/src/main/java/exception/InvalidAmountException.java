package exception;

/** Thrown for a zero, negative or non-numeric deposit/withdrawal/transfer amount. */
public class InvalidAmountException extends BankException {

    private static final long serialVersionUID = 1L;

    public InvalidAmountException(long amount) {
        super("Invalid amount: " + amount + " (must be greater than 0)");
    }

    /** Practical 14: for text typed by the user that is not a positive whole number. */
    public InvalidAmountException(String text) {
        super("Invalid amount: '" + text + "' (enter a positive whole number)");
    }
}
