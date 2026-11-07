package exception;

/**
 * Checked base class for every MiniBank failure. Callers must catch it or
 * declare it with throws.
 */
public class BankException extends Exception {

    private static final long serialVersionUID = 1L;

    public BankException(String message) {
        super(message);
    }
}
