package service;

import static util.Validator.isValidIfsc;
import static util.Validator.isValidMobile;

import java.util.Scanner;
import model.Account;
import model.BankInfo;
import model.CurrentAccount;
import model.FixedDepositAccount;
import model.InterestBearing;
import model.MenuOption;
import model.Premium;
import model.SavingsAccount;
import model.Transactable;
import model.WithdrawRule;

/**
 * MiniBank - console banking application.
 * Practical 01: menu shell.
 * Practical 02: Customer and Account classes (encapsulation).
 * Practical 03: toString(), equals()/hashCode(), nested Address, clone(), instanceof.
 * Practical 04: Validator (regex), Command record, CommandParser, StatementFormatter.
 * Practical 05: abstract Account with SavingsAccount, CurrentAccount, FixedDepositAccount.
 * Practical 06: packages model/util/service, interfaces, static import, runnable JAR.
 */
public class MiniBank {

    public static void main(String[] args) {
        BankInfo bank = new BankInfo("MiniBank", "Changa Branch");
        System.out.println("=== " + bank.name() + " - " + bank.branch() + " ===");
        System.out.println(bank);

        runPracticalDemo();
        runMenu(bank);
    }

    /** Practical 06 demo: interfaces, anonymous class vs lambda, marker interface. */
    private static void runPracticalDemo() {
        System.out.println();
        System.out.println("--- Practical 06: Interfaces and packages ---");
        Account[] accounts = {
            new SavingsAccount("Riya", 50000, 1000),
            new CurrentAccount("Aman", 8000, 5000),
            new FixedDepositAccount("Neha", 100000)
        };

        // 1. Account implements Transactable: use it through the interface type
        Transactable wallet = accounts[0];
        wallet.deposit(5000);
        System.out.println("Transactable deposit(5000): balance " + accounts[0].getBalance());

        // 2. InterestBearing default and static methods
        for (Account account : accounts) {
            System.out.println(account.getAccountNumber() + " yearlyInterest() = "
                    + account.yearlyInterest() + ", projectedBalance() = "
                    + account.projectedBalance());
        }
        System.out.println("InterestBearing.interestOn(10000, 4.0) = "
                + InterestBearing.interestOn(10000, 4.0));

        // 3. Functional interface: first an anonymous class, then a lambda
        WithdrawRule anonymousRule = new WithdrawRule() {
            public boolean allow(Account account, long amount) {
                return account.isActive() && amount <= 20000 && account.canWithdraw(amount);
            }
        };
        WithdrawRule lambdaRule = (account, amount) ->
                account.isActive() && amount <= 20000 && account.canWithdraw(amount);
        long[] amounts = {15000, 25000};
        for (long amount : amounts) {
            for (Account account : accounts) {
                System.out.println("allow(" + account.getAccountNumber() + ", " + amount
                        + "): anonymous = " + anonymousRule.allow(account, amount)
                        + ", lambda = " + lambdaRule.allow(account, amount));
            }
        }

        // 4. Marker interface
        for (Account account : accounts) {
            System.out.println(account.getAccountNumber() + " premium? "
                    + (account instanceof Premium));
        }

        // 5. Static import: Validator methods used without the class name
        System.out.println("isValidMobile(\"9876543210\") = " + isValidMobile("9876543210")
                + ", isValidIfsc(\"SBIN0001234\") = " + isValidIfsc("SBIN0001234"));
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
