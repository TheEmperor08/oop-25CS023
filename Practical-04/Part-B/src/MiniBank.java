import java.util.Scanner;

/**
 * MiniBank - console banking application.
 * Practical 01: menu shell.
 * Practical 02: Customer and Account classes (encapsulation).
 * Practical 03: toString(), equals()/hashCode(), nested Address, clone(), instanceof.
 * Practical 04: Validator (regex), Command record, CommandParser, StatementFormatter.
 */
public class MiniBank {

    public static void main(String[] args) {
        BankInfo bank = new BankInfo("MiniBank", "Changa Branch");
        System.out.println("=== " + bank.name() + " - " + bank.branch() + " ===");
        System.out.println(bank);

        runPracticalDemo();
        runMenu(bank);
    }

    /** Practical 04 demo: regex validators, command parsing, StringBuilder statement. */
    private static void runPracticalDemo() {
        System.out.println();
        System.out.println("--- Practical 04: Validation and command parsing ---");
        // one correct and one wrong input for every validator
        show("isValidMobile", "9876543210", Validator.isValidMobile("9876543210"));
        show("isValidMobile", "12345", Validator.isValidMobile("12345"));
        show("isValidEmail", "abc@xyz.com", Validator.isValidEmail("abc@xyz.com"));
        show("isValidEmail", "abc@xyz", Validator.isValidEmail("abc@xyz"));
        show("isValidPan", "ABCDE1234F", Validator.isValidPan("ABCDE1234F"));
        show("isValidPan", "ABCD12345F", Validator.isValidPan("ABCD12345F"));
        show("isValidIfsc", "SBIN0001234", Validator.isValidIfsc("SBIN0001234"));
        show("isValidIfsc", "SBIN1001234", Validator.isValidIfsc("SBIN1001234"));
        show("isValidAmount", "500", Validator.isValidAmount("500"));
        show("isValidAmount", "-50", Validator.isValidAmount("-50"));

        Command command = CommandParser.parse("DEPOSIT AC0001 500");
        System.out.println("Parsed: type=" + command.type() + ", accountNumber="
                + command.accountNumber() + ", amount=" + command.amount());
        System.out.println("Record toString: " + command);
        try {
            CommandParser.parse("WITHDRAW AC0001");
        } catch (IllegalArgumentException e) {
            System.out.println("Parse error: " + e.getMessage());
        }

        Account account = new Account("Riya", 4000);
        System.out.println(StatementFormatter.buildStatement(account));
    }

    /** Prints one validator call and its result, e.g. isValidMobile("12345") = false. */
    private static void show(String method, String input, boolean result) {
        System.out.printf("%-28s = %b%n", method + "(\"" + input + "\")", result);
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
