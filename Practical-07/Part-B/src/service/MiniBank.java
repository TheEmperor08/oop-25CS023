package service;

import java.util.Arrays;
import java.util.Scanner;
import model.Account;
import model.BankInfo;
import model.CurrentAccount;
import model.Customer;
import model.MenuOption;
import model.SavingsAccount;
import model.WithdrawRule;
import util.AnnotationValidator;

/**
 * MiniBank - console banking application.
 * Practical 01: menu shell.
 * Practical 02: Customer and Account classes (encapsulation).
 * Practical 03: toString(), equals()/hashCode(), nested Address, clone(), instanceof.
 * Practical 04: Validator (regex), Command record, CommandParser, StatementFormatter.
 * Practical 05: abstract Account with SavingsAccount, CurrentAccount, FixedDepositAccount.
 * Practical 06: packages model/util/service, interfaces, static import, runnable JAR.
 * Practical 07: @Override/@FunctionalInterface, custom annotations, AnnotationValidator.
 */
public class MiniBank {

    public static void main(String[] args) {
        BankInfo bank = new BankInfo("MiniBank", "Changa Branch");
        System.out.println("=== " + bank.name() + " - " + bank.branch() + " ===");
        System.out.println(bank);

        runPracticalDemo();
        runMenu(bank);
    }

    /** Practical 07 demo: custom annotations validated by reflection. */
    private static void runPracticalDemo() {
        System.out.println();
        System.out.println("--- Practical 07: Annotations and reflection ---");
        Account invalid = new SavingsAccount("Test", -100, 0);
        Account valid = new SavingsAccount("Riya", 5000, 1000);
        printErrors("Account balance -100", AnnotationValidator.validate(invalid));
        printErrors("Account balance 5000", AnnotationValidator.validate(valid));

        // Supplementary: @MaxLength(10) on Customer.mobile, errors with field names
        Customer good = new Customer("Riya Shah", "riya@example.com", "9876543210");
        Customer bad = new Customer("Aman", "aman@example.com", "98765432101");
        printErrors("Customer mobile 9876543210 (field names)",
                AnnotationValidator.validateWithFieldNames(good));
        printErrors("Customer mobile 98765432101 (field names)",
                AnnotationValidator.validateWithFieldNames(bad));
        Account both = new CurrentAccount("A very long owner name for one account", 0, 1000);
        printErrors("Account with long owner and balance 0 (field names)",
                AnnotationValidator.validateWithFieldNames(both));

        // Standard annotations: @FunctionalInterface lets the lambda target WithdrawRule
        WithdrawRule rule = (account, amount) -> amount <= account.getBalance();
        System.out.println("WithdrawRule (@FunctionalInterface) allow(valid, 3000) = "
                + rule.allow(valid, 3000));
    }

    /** Prints the returned String[] of validation errors. */
    private static void printErrors(String label, String[] errors) {
        System.out.println(label + " -> " + errors.length + " error(s) "
                + Arrays.toString(errors));
    }

    // ---- end of practical demo ----

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
