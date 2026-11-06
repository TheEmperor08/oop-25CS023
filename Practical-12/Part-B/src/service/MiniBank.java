package service;

import exception.AccountNotFoundException;
import exception.BankException;
import java.util.Scanner;
import model.Account;
import model.BankInfo;
import model.Command;
import model.CurrentAccount;
import model.FixedDepositAccount;
import model.MenuOption;
import model.SavingsAccount;
import model.TransactionType;

/**
 * MiniBank - console banking application.
 * Practical 01: menu shell.
 * Practical 02: Customer and Account classes (encapsulation).
 * Practical 03: toString(), equals()/hashCode(), nested Address, clone(), instanceof.
 * Practical 04: Validator (regex), Command record, CommandParser, StatementFormatter.
 * Practical 05: abstract Account with SavingsAccount, CurrentAccount, FixedDepositAccount.
 * Practical 06: packages model/util/service, interfaces, static import, runnable JAR.
 * Practical 07: @Override/@FunctionalInterface, custom annotations, AnnotationValidator.
 * Practical 08: exception package, checked exceptions, try-with-resources (BankSession).
 * Practical 09: AccountWorker threads, race condition, synchronized deposit/withdraw.
 * Practical 10: TransactionProcessor (thread pool), producer-consumer, deadlock fix.
 * Practical 11: Serializable model, StatePersister, TransactionLog, ReportGenerator.
 * Practical 12: AccountStore (HashMap/ArrayList), Comparable/Comparator, ConcurrentHashMap.
 */
public class MiniBank {

    public static void main(String[] args) throws BankException, InterruptedException {
        BankInfo bank = new BankInfo("MiniBank", "Changa Branch");
        System.out.println("=== " + bank.name() + " - " + bank.branch() + " ===");
        System.out.println(bank);

        runPracticalDemo();
        runMenu(bank);
    }

    /** Practical 12 demo: Map-based store, sorting, thread-safe collection. */
    private static void runPracticalDemo() throws BankException, InterruptedException {
        System.out.println();
        System.out.println("--- Practical 12: Collections ---");
        AccountStore store = new AccountStore();
        store.add(new SavingsAccount("Riya", 5000, 1000));
        store.add(new CurrentAccount("Aman", 2000, 5000));
        store.add(new FixedDepositAccount("Neha", 9000));

        System.out.println("get(\"AC0002\") -> " + store.get("AC0002"));
        try {
            store.get("AC0999");
        } catch (AccountNotFoundException e) {
            System.out.println("get(\"AC0999\") -> " + e.getMessage());
        }

        Account riya = store.get("AC0001");
        riya.deposit(500);
        store.record("DEPOSIT AC0001 500");
        riya.withdraw(500);
        store.record("WITHDRAW AC0001 500");

        System.out.println("Sorted by balance (Comparator, highest first):");
        for (Account account : store.sortedByBalance()) {
            System.out.println("  " + account);
        }
        System.out.println("TreeMap keys (kept sorted): " + store.sortedByNumber().keySet());
        System.out.println("Balance above 3000 (sorted by Comparable compareTo): "
                + store.balanceAbove(3000));
        System.out.println("History (ArrayList): " + store.getHistory());

        // ConcurrentHashMap inside the TransactionProcessor: 4 pool threads update
        // the shared accounts at the same time
        TransactionProcessor processor = new TransactionProcessor();
        for (Account account : store.getAll()) {
            processor.register(account);
        }
        for (int i = 0; i < 300; i++) {
            String number = (i % 2 == 0) ? "AC0001" : "AC0002";
            processor.submit(new Command(TransactionType.DEPOSIT, number, 10));
        }
        processor.submit(new Command(TransactionType.DEPOSIT, "AC0999", 10));   // unknown
        processor.stop();
        System.out.println("After 300 concurrent deposits of 10: AC0001 = "
                + processor.getAccount("AC0001").getBalance() + " (expected 6500), AC0002 = "
                + processor.getAccount("AC0002").getBalance() + " (expected 3500), failures = "
                + processor.getFailures());
    }

    // ---- end of practical demo ----

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
