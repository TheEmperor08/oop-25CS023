package service;

/**
 * A small resource for try-with-resources: a teller session that must always be
 * closed, even when an operation inside it throws an exception.
 */
public class BankSession implements AutoCloseable {

    private final String teller;

    public BankSession(String teller) {
        this.teller = teller;
        System.out.println("  Session opened for " + teller);
    }

    public void log(String message) {
        System.out.println("  [" + teller + "] " + message);
    }

    /** Called automatically at the end of the try-with-resources block. */
    @Override
    public void close() {
        System.out.println("  Session closed for " + teller);
    }
}
