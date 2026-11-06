package discount.app;

import static discount.rule.RuleCatalog.NAMES;

import discount.rule.DiscountRule;
import discount.rule.RuleCatalog;
import java.util.List;
import java.util.Scanner;

/**
 * Practical 06 - Part A - Program 3 (For Advanced Learners): Discount engine.
 * The user picks discount rules (lambdas) at runtime; each picked rule is applied
 * to the price list. Packaged as a runnable JAR (Main-Class: discount.app.DiscountApp).
 */
public class DiscountApp {

    public static void main(String[] args) {
        List<Double> prices = List.of(250.0, 799.0, 1200.0, 4999.0);
        Scanner scanner = new Scanner(System.in);
        DiscountRule combined = price -> price;   // identity: no discount yet

        System.out.println("Prices: " + prices);
        System.out.println("Rules:");
        for (int i = 0; i < NAMES.length; i++) {
            System.out.println("  " + (i + 1) + ". " + NAMES[i]);
        }
        while (true) {
            System.out.print("Pick a rule number (0 to finish): ");
            if (!scanner.hasNextLine()) {
                break;
            }
            String line = scanner.nextLine().trim();
            int number;
            try {
                number = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Not a number: " + line);
                continue;
            }
            if (number == 0) {
                break;
            }
            DiscountRule rule = RuleCatalog.pick(number);
            if (rule == null) {
                System.out.println("No such rule: " + number);
                continue;
            }
            System.out.println("Applying: " + NAMES[number - 1]);
            printPrices(prices, rule);
            combined = combined.andThen(rule);
        }
        System.out.println("All picked rules chained together:");
        printPrices(prices, combined);
        scanner.close();
    }

    private static void printPrices(List<Double> prices, DiscountRule rule) {
        double before = 0;
        double after = 0;
        for (double price : prices) {
            double discounted = rule.apply(price);
            System.out.printf("  %8.2f -> %8.2f%n", price, discounted);
            before += price;
            after += discounted;
        }
        System.out.printf("  Total %8.2f -> %8.2f (saved %.2f)%n",
                before, after, before - after);
    }
}
