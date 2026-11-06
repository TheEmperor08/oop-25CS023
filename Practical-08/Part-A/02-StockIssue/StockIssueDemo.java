/**
 * Practical 08 - Part A - Program 2: Stock issue.
 * Processes a list of issue requests; every failure is caught and reported
 * without stopping the run.
 */
public class StockIssueDemo {

    /** One issue request (read-only data). */
    record Request(String item, int qty) { }

    public static void main(String[] args) {
        Warehouse warehouse = new Warehouse();
        warehouse.receive("bolts", 100);
        warehouse.receive("nuts", 40);
        warehouse.receive("washers", 10);
        System.out.println("Start: " + warehouse);

        Request[] requests = {
            new Request("bolts", 30),
            new Request("nuts", 50),
            new Request("washers", 0),
            new Request("washers", 10),
            new Request("screws", 5),
            new Request("bolts", -2),
            new Request("bolts", 70)
        };
        int ok = 0;
        int failed = 0;
        for (Request r : requests) {
            try {
                warehouse.issue(r.item(), r.qty());
                System.out.println("ISSUED  " + r.qty() + " " + r.item());
                ok++;
            } catch (OutOfStockException e) {
                System.out.println("REFUSED " + r.qty() + " " + r.item() + ": "
                        + e.getMessage()
                        + " (shortfall " + e.getShortfall() + ")");
                failed++;
            } catch (InvalidQuantityException e) {
                System.out.println("INVALID " + r.item() + ": " + e.getMessage());
                failed++;
            }
        }
        System.out.println("Processed " + requests.length + " requests: " + ok + " issued, "
                + failed + " failed");
        System.out.println("End: " + warehouse);
    }
}
