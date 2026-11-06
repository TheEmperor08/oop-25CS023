/**
 * Practical 08 - Part A - Program 3 (For Advanced Learners): try-with-resources.
 * Proves that an AutoCloseable resource is always closed - even when the block
 * throws - and that the original error is the one reported (a failure inside
 * close() is attached to it as a suppressed exception).
 */
public class TryWithResourcesDemo {

    public static void main(String[] args) {
        System.out.println("1) Normal use:");
        PrinterSession kept;
        try (PrinterSession p = new PrinterSession("Printer-A", false)) {
            p.print("Invoice 101");
            kept = p;
        }
        System.out.println("  still open after block? " + kept.isOpen());

        System.out.println("2) Exception thrown inside the block:");
        PrinterSession second = null;
        try (PrinterSession p = new PrinterSession("Printer-B", false)) {
            second = p;
            p.print("");
        } catch (IllegalArgumentException e) {
            System.out.println("  caught: " + e.getMessage());
        }
        System.out.println("  still open after exception? " + second.isOpen());

        System.out.println("3) Block throws AND close() throws:");
        try (PrinterSession p = new PrinterSession("Printer-C", true)) {
            p.print("");
        } catch (RuntimeException e) {
            System.out.println("  reported: " + e.getMessage());
            for (Throwable s : e.getSuppressed()) {
                System.out.println("  suppressed: " + s.getMessage());
            }
        }

        System.out.println("4) Two resources are closed in reverse order:");
        try (PrinterSession first = new PrinterSession("Printer-D", false);
             PrinterSession other = new PrinterSession("Printer-E", false)) {
            first.print("Report part 1");
            other.print("Report part 2");
        }
    }
}
