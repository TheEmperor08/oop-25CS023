import java.util.Scanner;

/**
 * MiniBank - console banking application.
 * Practical 01: menu shell.
 * Practical 02: Customer and Account classes (encapsulation); main creates an
 *               Account[] array and performs deposits and withdrawals.
 */
public class MiniBank {

    public static void main(String[] args) {
        BankInfo bank = new BankInfo("MiniBank", "Changa Branch");
        System.out.println("=== " + bank.name() + " - " + bank.branch() + " ===");
        System.out.println(bank);

        runPracticalDemo();
        runMenu(bank);
    }

    /** Practical 02 demo: three accounts in an Account[] array. */
    private static void runPracticalDemo() {
        System.out.println();
        System.out.println("--- Practical 02: Customer and Account ---");
        Customer customer = new Customer("Riya Shah", "riya@example.com", "9876543210");
        System.out.println("Customer " + customer.getCustomerId() + ": " + customer.getName()
                + ", " + customer.getEmail() + ", " + customer.getMobile());

        Account[] accounts = {
            new Account("Riya", 5000),
            new Account("Aman"),               // chained constructor: opening balance 0
            new Account("Neha", 12000)
        };

        Account riya = accounts[0];
        riya.deposit(2000);
        System.out.println("After deposit(2000):      balance = " + riya.getBalance());
        boolean ok = riya.withdraw(3000);
        System.out.println("withdraw(3000) returned " + ok + ", balance = " + riya.getBalance());
        ok = riya.withdraw(10000);
        System.out.println("withdraw(10000) returned " + ok + ", balance = " + riya.getBalance());

        accounts[1].deposit(1500);
        accounts[1].deposit(-200);             // supplementary: negative amount rejected
        accounts[2].withdraw(2500);
        System.out.println("transfer(Neha -> Aman, 1000) returned "
                + transfer(accounts[2], accounts[1], 1000));

        for (Account account : accounts) {
            System.out.println(account.getAccountNumber() + " " + account.getOwnerName()
                    + " balance = " + account.getBalance() + " active = " + account.isActive());
        }
    }

    /**
     * Supplementary: moves an amount from one account to another using
     * withdraw and deposit. Nothing changes if the withdrawal fails.
     */
    static boolean transfer(Account from, Account to, long amount) {
        if (amount <= 0 || !from.withdraw(amount)) {
            return false;
        }
        to.deposit(amount);
        return true;
    }

    /** The Practical 01 menu loop: placeholders until EXIT is chosen. */
    private static void runMenu(BankInfo bank) {
        Scanner scanner = new Scanner(System.in);
        MenuOption option;
        do {
            printMenu();
            option = readOption(scanner);
            if (option == null) {          // input ended
                break;
            }
            String message = switch (option) {
                case OPEN_ACCOUNT -> "Open account - to be implemented in a later lab";
                case DEPOSIT -> "Deposit - to be implemented in a later lab";
                case WITHDRAW -> "Withdraw - to be implemented in a later lab";
                case TRANSFER -> "Transfer - to be implemented in a later lab";
                case WORKING_HOURS -> "Working hours: Mon-Fri 10:00-17:00, Sat 10:00-13:00";
                case EXIT -> "Thank you for banking with " + bank.name() + ". Goodbye!";
            };
            System.out.println(message);
        } while (option != MenuOption.EXIT);
        scanner.close();
    }

    /** Prints the numbered menu built from the enum constants. */
    private static void printMenu() {
        System.out.println();
        MenuOption[] options = MenuOption.values();
        for (int i = 0; i < options.length; i++) {
            System.out.println((i + 1) + ". " + options[i]);
        }
        System.out.print("Enter choice: ");
    }

    /**
     * Reads a menu number and maps it to a MenuOption with a switch expression.
     * Re-prompts until a valid number is entered; returns null at end of input.
     */
    private static MenuOption readOption(Scanner scanner) {
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            int choice;
            try {
                choice = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                choice = -1;
            }
            MenuOption option = switch (choice) {
                case 1 -> MenuOption.OPEN_ACCOUNT;
                case 2 -> MenuOption.DEPOSIT;
                case 3 -> MenuOption.WITHDRAW;
                case 4 -> MenuOption.TRANSFER;
                case 5 -> MenuOption.WORKING_HOURS;
                case 6 -> MenuOption.EXIT;
                default -> null;
            };
            if (option != null) {
                return option;
            }
            System.out.print("Invalid choice '" + line + "'. Enter a number from 1 to "
                    + MenuOption.values().length + ": ");
        }
        return null;
    }
}
