package service;

import exception.BankException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import model.Account;
import model.BankInfo;
import model.MenuOption;

/**
 * MiniBank - console banking application (final version, Practical 14).
 * Practical 01: menu shell (BankInfo record, MenuOption enum, switch expressions).
 * Practical 02-13: model, validation, annotations, exceptions, interfaces/packages,
 *                  threads, persistence, collections and the generic repository.
 * Practical 14: every menu option calls a BankService operation; accounts are loaded
 *               on start-up and saved on exit; batch processing and the report run
 *               from the menu.
 */
public class MiniBank {

    private final BankInfo bank = new BankInfo("MiniBank", "Changa Branch");
    private final BankService service;
    private final Scanner scanner = new Scanner(System.in);

    public MiniBank(BankService service) {
        this.service = service;
    }

    public static void main(String[] args) {
        Path dataDir = Paths.get(args.length > 0 ? args[0] : "data");
        new MiniBank(new BankService(dataDir)).run();
    }

    /** Loads saved data, shows the menu until EXIT (or end of input), then saves. */
    public void run() {
        System.out.println("=== " + bank.name() + " - " + bank.branch() + " ===");
        try {
            System.out.println("Loaded " + service.load() + " account(s) from saved data.");
        } catch (IOException e) {
            System.out.println("Could not load saved data: " + e.getMessage());
        }

        MenuOption option;
        do {
            printMenu();
            option = readOption();
            if (option == null) {                        // end of input
                break;
            }
            String message = switch (option) {
                case OPEN_ACCOUNT -> openAccount();
                case DEPOSIT -> deposit();
                case WITHDRAW -> withdraw();
                case TRANSFER -> transfer();
                case STATEMENT -> statement();
                case LIST_ACCOUNTS -> listAccounts(service.listAccounts());
                case SEARCH_BY_OWNER ->
                    listAccounts(service.searchByOwner(ask("Owner name contains: ")));
                case BATCH_PROCESS -> batch();
                case REPORT -> report();
                case WORKING_HOURS -> "Working hours: Mon-Fri 10:00-17:00, Sat 10:00-13:00";
                case EXIT -> "Thank you for banking with " + bank.name() + ". Goodbye!";
            };
            System.out.println(message);
        } while (option != MenuOption.EXIT);

        try {
            System.out.println("Saved " + service.save() + " account(s).");
        } catch (IOException e) {
            System.out.println("Could not save data: " + e.getMessage());
        }
    }

    // ------------------------------------------------------------ menu actions
    // Each action returns the message to print; failures are caught and reported.

    private String openAccount() {
        String type = switch (ask("Type (1=Savings, 2=Current, 3=Fixed deposit): ")) {
            case "1" -> "SAVINGS";
            case "2" -> "CURRENT";
            case "3" -> "FD";
            default -> "?";
        };
        try {
            String owner = ask("Owner name: ");
            long opening = BankService.parseAmount(ask("Opening balance: "));
            Account account = service.openAccount(type, owner, opening);
            return "Opened " + account.getClass().getSimpleName() + " " + account;
        } catch (BankException e) {
            return "Error: " + e.getMessage();
        }
    }

    private String deposit() {
        try {
            String number = ask("Account number: ");
            long amount = BankService.parseAmount(ask("Amount: "));
            service.deposit(number, amount);
            return "Deposited " + amount + ". New balance: " + service.find(number).getBalance();
        } catch (BankException e) {
            return "Error: " + e.getMessage();
        }
    }

    private String withdraw() {
        try {
            String number = ask("Account number: ");
            long amount = BankService.parseAmount(ask("Amount: "));
            service.withdraw(number, amount);
            return "Withdrew " + amount + ". New balance: " + service.find(number).getBalance();
        } catch (BankException e) {
            return "Error: " + e.getMessage();
        }
    }

    private String transfer() {
        try {
            String from = ask("From account: ");
            String to = ask("To account: ");
            long amount = BankService.parseAmount(ask("Amount: "));
            service.transfer(from, to, amount);
            return "Transferred " + amount + ". Balances: " + service.find(from).getBalance()
                    + " / " + service.find(to).getBalance();
        } catch (BankException e) {
            return "Error: " + e.getMessage();
        }
    }

    private String statement() {
        try {
            return service.statement(ask("Account number: "));
        } catch (BankException e) {
            return "Error: " + e.getMessage();
        }
    }

    private String listAccounts(List<Account> accounts) {
        if (accounts.isEmpty()) {
            return "No accounts found.";
        }
        StringBuilder sb = new StringBuilder();
        for (Account account : accounts) {
            sb.append(String.format("  %-20s %s%n", account.getClass().getSimpleName(), account));
        }
        return sb.append(accounts.size()).append(" account(s)").toString();
    }

    /** Runs a batch from a file, or a built-in sample batch, on the thread pool. */
    private String batch() {
        List<String> lines;
        String file = ask("Batch file (press Enter for the built-in sample): ");
        try {
            if (file.isBlank()) {
                lines = sampleBatch();
            } else if (Files.exists(Paths.get(file))) {
                lines = service.readBatchFile(Paths.get(file));
            } else {
                return "Error: file not found: " + file;
            }
            if (lines.isEmpty()) {
                return "Nothing to process (no accounts or empty batch).";
            }
            long start = System.currentTimeMillis();
            int[] result = service.processBatch(lines);
            return "Batch of " + lines.size() + " transactions on 4 threads: " + result[0]
                    + " succeeded, " + result[1] + " failed ("
                    + (System.currentTimeMillis() - start) + " ms)" + System.lineSeparator() + listAccounts(service.listAccounts());
        } catch (IOException e) {
            return "Error: " + e.getMessage();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "Error: batch interrupted";
        }
    }

    /** 100 deposits of 10 and 50 withdrawals of 10 for every account. */
    private List<String> sampleBatch() {
        List<String> lines = new ArrayList<>();
        for (Account account : service.listAccounts()) {
            for (int i = 0; i < 150; i++) {
                String type = (i % 3 == 2) ? "WITHDRAW " : "DEPOSIT ";
                lines.add(type + account.getAccountNumber() + " 10");
            }
        }
        return lines;
    }

    private String report() {
        try {
            return service.report();
        } catch (IOException e) {
            return "Error: " + e.getMessage();
        }
    }

    // ------------------------------------------------------------ input helpers

    /** Prints the numbered menu (built from the enum constants) in three columns. */
    private void printMenu() {
        System.out.println();
        MenuOption[] options = MenuOption.values();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < options.length; i++) {
            sb.append(String.format("%2d. %-17s", i + 1, options[i]));
            if (i % 3 == 2 || i == options.length - 1) {
                System.out.println(sb.toString().stripTrailing());
                sb.setLength(0);
            }
        }
        System.out.print("Enter choice: ");
    }

    /**
     * Reads a menu number and maps it to a MenuOption with a switch expression.
     * Re-prompts until a valid number is entered; returns null at end of input.
     */
    private MenuOption readOption() {
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
                case 5 -> MenuOption.STATEMENT;
                case 6 -> MenuOption.LIST_ACCOUNTS;
                case 7 -> MenuOption.SEARCH_BY_OWNER;
                case 8 -> MenuOption.BATCH_PROCESS;
                case 9 -> MenuOption.REPORT;
                case 10 -> MenuOption.WORKING_HOURS;
                case 11 -> MenuOption.EXIT;
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

    /** Prints a prompt and returns the typed line ("" at end of input). */
    private String ask(String prompt) {
        System.out.print(prompt);
        return scanner.hasNextLine() ? scanner.nextLine().trim() : "";
    }
}
