import java.util.Scanner;

/**
 * Practical 01 - Part A - Program 1: Vending machine.
 * Reads coin names until the snack price (15) is paid, then prints the change.
 */
public class VendingMachine {

    /** The coins the machine accepts. */
    enum Coin { ONE, TWO, FIVE, TEN }

    public static void main(String[] args) {
        final int price = 15;
        int total = 0;
        Scanner scanner = new Scanner(System.in);

        System.out.println("Snack price: " + price);
        while (total < price) {
            System.out.print("Insert coin (ONE, TWO, FIVE, TEN): ");
            if (!scanner.hasNext()) {
                break;
            }
            String name = scanner.next().trim().toUpperCase();
            Coin coin;
            try {
                coin = Coin.valueOf(name);
            } catch (IllegalArgumentException e) {
                System.out.println("Unknown coin: " + name);
                continue;
            }
            int value = switch (coin) {
                case ONE -> 1;
                case TWO -> 2;
                case FIVE -> 5;
                case TEN -> 10;
            };
            total += value;
            System.out.println("Total so far: " + total);
        }
        if (total >= price) {
            System.out.println("Paid. Change: " + (total - price));
        }
        scanner.close();
    }
}
