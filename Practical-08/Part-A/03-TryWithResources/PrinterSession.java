/**
 * Practical 08 - Part A - Program 3 (For Advanced Learners): try-with-resources.
 * Models a shared office printer that must always be released. close() can be
 * told to fail so that suppressed exceptions can be demonstrated.
 */
public class PrinterSession implements AutoCloseable {

    private final String name;
    private final boolean failOnClose;
    private boolean open;

    public PrinterSession(String name, boolean failOnClose) {
        this.name = name;
        this.failOnClose = failOnClose;
        this.open = true;
        System.out.println("  [" + name + "] opened");
    }

    public void print(String document) {
        if (!open) {
            throw new IllegalStateException(name + " is closed");
        }
        if (document.isEmpty()) {
            throw new IllegalArgumentException("empty document sent to " + name);
        }
        System.out.println("  [" + name + "] printing \"" + document + "\"");
    }

    public boolean isOpen() {
        return open;
    }

    @Override
    public void close() {
        open = false;
        System.out.println("  [" + name + "] closed");
        if (failOnClose) {
            throw new IllegalStateException(name + " jammed while closing");
        }
    }
}
