/**
 * Practical 08 - Part A - Program 2: Stock issue.
 * Checked exception carrying how many units are missing (the shortfall).
 */
public class OutOfStockException extends Exception {

    private static final long serialVersionUID = 1L;
    private final int shortfall;

    public OutOfStockException(String item, int requested, int available) {
        super("only " + available + " " + item + " in stock, requested " + requested);
        this.shortfall = requested - available;
    }

    public int getShortfall() {
        return shortfall;
    }
}
