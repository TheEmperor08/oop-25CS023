package service;

import exception.AccountNotFoundException;
import exception.BankException;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import java.util.Scanner;
import model.Account;
import model.BankInfo;
import model.CurrentAccount;
import model.MenuOption;
import model.SavingsAccount;

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
 */
public class MiniBank {

    public static void main(String[] args) {
        BankInfo bank = new BankInfo("MiniBank", "Changa Branch");
        System.out.println("=== " + bank.name() + " - " + bank.branch() + " ===");
        System.out.println(bank);

        runPracticalDemo();
        runMenu(bank);
    }

    /** Practical 08 demo: custom exceptions, try-catch-finally, try-with-resources. */
    private static void runPracticalDemo() {
        System.out.println();
        System.out.println("--- Practical 08: Exception handling ---");
        Account riya = new SavingsAccount("Riya", 1000, 0);
        Account aman = new CurrentAccount("Aman", 2000, 1000);
        Account[] accounts = {riya, aman};

        // 1. withdraw more than the balance -> InsufficientFundsException
        try {
            riya.withdraw(5000);
            System.out.println("Withdrawal succeeded");
        } catch (InsufficientFundsException e) {
            System.out.println("Withdrawal failed: short by " + e.getShortfall());
        } catch (BankException e) {
            System.out.println("Withdrawal failed: " + e.getMessage());
        } finally {
            System.out.println("finally: balance of " + riya.getAccountNumber() + " is "
                    + riya.getBalance());
        }

        // 2. non-positive deposit -> InvalidAmountException
        try {
            riya.deposit(-50);
        } catch (InvalidAmountException e) {
            System.out.println("Deposit failed: " + e.getMessage());
        }

        // 3. unknown account -> AccountNotFoundException
        try {
            findAccount(accounts, "AC9999");
        } catch (AccountNotFoundException e) {
            System.out.println("Lookup failed: " + e.getMessage());
        }

        // 4. transfer: one success, one failure that is rolled back and re-thrown
        try {
            aman.transfer(riya, 2500);                 // uses 500 of the overdraft
            System.out.println("Transfer 1 done: Aman " + aman.getBalance()
                    + ", Riya " + riya.getBalance());
            aman.transfer(riya, 1000);                 // only 500 overdraft left
        } catch (BankException e) {
            System.out.println("Transfer 2 failed: " + e.getMessage());
        } finally {
            System.out.println("After transfers: Aman " + aman.getBalance()
                    + ", Riya " + riya.getBalance());
        }

        // 5. supplementary: daily withdrawal limit
        Account rich = new SavingsAccount("Neha", 200000, 1000);
        try {
            rich.withdraw(40000);
            rich.withdraw(20000);
        } catch (BankException e) {
            System.out.println(e.getClass().getSimpleName() + ": " + e.getMessage());
        }

        // 6. try-with-resources: the session is closed even though an exception is thrown
        try (BankSession session = new BankSession("Teller-1")) {
            session.log("withdrawing 0 from " + riya.getAccountNumber());
            riya.withdraw(0);
        } catch (BankException e) {
            System.out.println("Caught after the session closed: " + e.getMessage());
        }
    }

    /** Looks an account up by number or throws AccountNotFoundException. */
    private static Account findAccount(Account[] accounts, String number)
            throws AccountNotFoundException {
        for (Account account : accounts) {
            if (account.getAccountNumber().equals(number)) {
                return account;
            }
        }
        throw new AccountNotFoundException(number);
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
