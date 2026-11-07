package service;

import exception.BankException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import model.Account;
import model.BankInfo;
import model.CurrentAccount;
import model.FixedDepositAccount;
import model.MenuOption;
import model.SavingsAccount;
import util.ReportGenerator;
import util.StatePersister;
import util.TransactionLog;

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
 */
public class MiniBank {

    public static void main(String[] args) throws IOException {
        BankInfo bank = new BankInfo("MiniBank", "Changa Branch");
        System.out.println("=== " + bank.name() + " - " + bank.branch() + " ===");
        System.out.println(bank);

        runPracticalDemo();
        runMenu(bank);
    }

    /** Practical 11 demo: save/load accounts, append-only logs, NIO end-of-day report. */
    private static void runPracticalDemo() throws IOException {
        System.out.println();
        System.out.println("--- Practical 11: Persistence and reporting ---");
        Path dataDir = Paths.get("data");
        Path accountsFile = dataDir.resolve("accounts.dat");
        Path logsDir = dataDir.resolve("logs");
        Path teller1 = logsDir.resolve("teller-1.log");
        Path teller2 = logsDir.resolve("teller-2.log");

        Account[] accounts;
        if (Files.exists(accountsFile)) {
            accounts = StatePersister.load(accountsFile);
            System.out.println("Loaded " + accounts.length + " accounts saved by the previous run:");
            for (Account account : accounts) {
                System.out.println("  " + account);
            }
        } else {
            System.out.println("No saved data yet - opening new accounts");
            accounts = new Account[] {
                new SavingsAccount("Riya", 5000, 1000),
                new CurrentAccount("Aman", 2000, 5000),
                new FixedDepositAccount("Neha", 50000)
            };
            Files.createDirectories(logsDir);
            Files.writeString(logsDir.resolve("notes.txt"), "not a log file\n");
            Files.writeString(logsDir.resolve("empty.log"), "");
        }
        Account riya = accounts[0];
        Account aman = accounts[1];
        Account neha = accounts[2];

        // Transactions; each successful one is appended to a teller's log file
        try {
            riya.deposit(2000);
            TransactionLog.append(teller1, "DEPOSIT " + riya.getAccountNumber() + " 2000");
            riya.withdraw(1500);
            TransactionLog.append(teller1, "WITHDRAW " + riya.getAccountNumber() + " 1500");
            aman.deposit(500);
            TransactionLog.append(teller2, "DEPOSIT " + aman.getAccountNumber() + " 500");
            aman.withdraw(4000);
            TransactionLog.append(teller2, "WITHDRAW " + aman.getAccountNumber() + " 4000");
            riya.transfer(aman, 1000);
            TransactionLog.append(teller1, "TRANSFER " + riya.getAccountNumber() + " "
                    + aman.getAccountNumber() + " 1000");
            neha.withdraw(1000);                               // locked fixed deposit
        } catch (BankException e) {
            System.out.println("  Not logged: " + e.getMessage());
        }
        System.out.println("Withdrawn today by " + riya.getAccountNumber() + " before saving: "
                + riya.getWithdrawnToday());

        // Save, then reload into a NEW array and compare
        StatePersister.save(accounts, accountsFile);
        Account[] reloaded = StatePersister.load(accountsFile);
        System.out.println("Saved to " + accountsFile + " (" + Files.size(accountsFile)
                + " bytes) and reloaded:");
        for (int i = 0; i < reloaded.length; i++) {
            System.out.println("  " + reloaded[i] + "   same object? " + (reloaded[i] == accounts[i])
                    + ", equal? " + reloaded[i].equals(accounts[i]));
        }
        System.out.println("Withdrawn today after reload (transient field): "
                + reloaded[0].getWithdrawnToday());

        System.out.println(ReportGenerator.generate(logsDir,
                dataDir.resolve("reports").resolve("eod-report.txt")));
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
