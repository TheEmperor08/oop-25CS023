package service;

import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import model.Account;
import model.BankInfo;
import model.CurrentAccount;
import model.Customer;
import model.FixedDepositAccount;
import model.MenuOption;
import model.SavingsAccount;
import util.Repository;

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
 * Practical 13: generic Repository<T> keyed by the @Id field (reflection).
 */
public class MiniBank {

    public static void main(String[] args) {
        BankInfo bank = new BankInfo("MiniBank", "Changa Branch");
        System.out.println("=== " + bank.name() + " - " + bank.branch() + " ===");
        System.out.println(bank);

        runPracticalDemo();
        runMenu(bank);
    }

    /** Practical 13 demo: one generic Repository class used for two types. */
    private static void runPracticalDemo() {
        System.out.println();
        System.out.println("--- Practical 13: Generic repository ---");
        Repository<Account> accounts = new Repository<>();
        accounts.save(new SavingsAccount("Riya", 5000, 1000));
        accounts.saveAll(new CurrentAccount("Aman", 2000, 5000),
                new FixedDepositAccount("Neha", 9000));        // @SafeVarargs method

        Account found = accounts.findById("AC0002");          // key read from @Id by reflection
        System.out.println("findById(\"AC0002\") -> " + found);
        System.out.println("findById(\"AC0999\") -> " + accounts.findById("AC0999"));
        System.out.println("findAll() (" + accounts.count() + " items):");
        List<Account> all = accounts.findAll();
        Collections.sort(all);
        for (Account account : all) {
            System.out.println("  " + account);
        }
        System.out.println("delete(\"AC0003\") -> " + accounts.delete("AC0003")
                + ", count() = " + accounts.count());

        // Supplementary: the SAME class, unchanged, stores customers by their @Id customerId
        Repository<Customer> customers = new Repository<>();
        customers.saveAll(new Customer("Riya Shah", "riya@example.com", "9876543210"),
                new Customer("Aman Patel", "aman@example.com", "9123456780"));
        Customer customer = customers.findById("CUST102");     // no cast needed
        System.out.println("Repository<Customer> findById(\"CUST102\") -> " + customer.getName()
                + ", count() = " + customers.count());

        // Bounded type parameter <N extends Number>
        Integer[] ints = {5000, 2000, 9000};
        Double[] doubles = {4.0, 0.0, 7.0};
        Long[] longs = {10L, 20L, 30L, 40L};
        System.out.println("average(Integer[]) = " + Repository.average(ints)
                + ", average(Double[]) = " + Repository.average(doubles)
                + ", average(Long[]) = " + Repository.average(longs));
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
