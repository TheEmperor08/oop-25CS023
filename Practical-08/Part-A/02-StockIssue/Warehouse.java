import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Practical 08 - Part A - Program 2: Stock issue.
 * Keeps stock levels and issues items, throwing custom exceptions on failure.
 */
public class Warehouse {

    private final Map<String, Integer> stock = new LinkedHashMap<>();

    public void receive(String item, int qty) {
        stock.merge(item, qty, Integer::sum);
    }

    /** Removes qty units of item; the stock is unchanged when an exception is thrown. */
    public void issue(String item, int qty) throws OutOfStockException {
        if (qty <= 0) {
            throw new InvalidQuantityException(qty);
        }
        int available = stock.getOrDefault(item, 0);
        if (qty > available) {
            throw new OutOfStockException(item, qty, available);
        }
        stock.put(item, available - qty);
    }

    @Override
    public String toString() {
        return "Stock " + stock;
    }
}
