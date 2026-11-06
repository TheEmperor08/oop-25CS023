/**
 * Practical 08 - Part A - Program 2: Stock issue.
 * Thrown when the requested quantity is zero or negative. It is unchecked
 * (extends RuntimeException) because a bad quantity is a caller error, which
 * contrasts with the checked OutOfStockException that callers must handle.
 */
public class InvalidQuantityException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidQuantityException(int qty) {
        super("quantity must be greater than 0, got " + qty);
    }
}
