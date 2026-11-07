package service;

import exception.AccountNotFoundException;
import exception.BankException;
import java.lang.management.ManagementFactory;
import java.util.Scanner;
import model.Account;
import model.BankInfo;
import model.Command;
import model.MenuOption;
import model.SavingsAccount;
import model.TransactionType;
import util.CommandParser;

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
 */
public class MiniBank {

    public static void main(String[] args) throws InterruptedException {
        BankInfo bank = new BankInfo("MiniBank", "Changa Branch");
        System.out.println("=== " + bank.name() + " - " + bank.branch() + " ===");
        System.out.println(bank);

        runPracticalDemo();
        runMenu(bank);
    }

    /** Practical 10 demo: thread pool, producer-consumer, deadlock and its fix. */
    private static void runPracticalDemo() throws InterruptedException {
        System.out.println();
        System.out.println("--- Practical 10: Transaction engine ---");

        // 1. Thread pool: 100 deposit/withdraw tasks on 4 reusable threads
        Account pooled = new SavingsAccount("Pool", 10000, 0);
        TransactionProcessor processor = new TransactionProcessor();
        for (int i = 0; i < 100; i++) {
            boolean deposit = i % 5 < 3;                       // 60 deposits, 40 withdrawals
            processor.submit(() -> {
                try {
                    if (deposit) {
                        pooled.deposit(100);
                    } else {
                        pooled.withdraw(50);
                    }
                } catch (BankException e) {
                    System.out.println("  task failed: " + e.getMessage());
                }
            });
        }
        processor.stop();
        System.out.println("Pool: 60 x deposit(100) + 40 x withdraw(50) on 10000 -> balance "
                + pooled.getBalance() + " (expected 14000)");
        System.out.println("Tasks per pool thread: " + processor.getTasksPerThread());

        // 2. Producer-consumer with wait()/notify() on a shared array buffer
        Account[] accounts = {new SavingsAccount("AC0101", "Riya", 5000, 0),
            new SavingsAccount("AC0102", "Aman", 3000, 0)};
        String[] lines = {"DEPOSIT AC0101 500", "WITHDRAW AC0102 1000", "DEPOSIT AC0102 250",
            "WITHDRAW AC0101 700", "DEPOSIT AC0101 1200", "WITHDRAW AC0102 400"};
        TransactionBuffer buffer = new TransactionBuffer(2);
        Thread producer = new Thread(() -> {
            try {
                for (String line : lines) {
                    buffer.put(CommandParser.parse(line));
                    System.out.println("  produced: " + line);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "producer");
        Thread consumer = new Thread(() -> {
            try {
                for (int i = 0; i < lines.length; i++) {
                    Command c = buffer.take();
                    Account account = findAccount(accounts, c.accountNumber());
                    if (c.type() == TransactionType.DEPOSIT) {
                        account.deposit(c.amount());
                    } else {
                        account.withdraw(c.amount());
                    }
                    System.out.println("  consumed: " + c.type() + " " + c.accountNumber() + " "
                            + c.amount() + " -> balance " + account.getBalance());
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (BankException e) {
                System.out.println("  consumer error: " + e.getMessage());
            }
        }, "consumer");
        producer.start();
        consumer.start();
        producer.join();
        consumer.join();

        // 3. Deadlock: A->B and B->A, each locking its own "from" account first
        Account a = new SavingsAccount("AC0201", "A", 5000, 0);
        Account b = new SavingsAccount("AC0202", "B", 5000, 0);
        System.out.println("Opposite lock order:");
        boolean deadlocked = runTransferPair(a, b, false);
        System.out.println(deadlocked ? "  DEADLOCK detected: both threads are blocked forever"
                : "  no deadlock this time");

        // 4. Fix: always lock the smaller account number first
        Account c = new SavingsAccount("AC0301", "C", 5000, 0);
        Account d = new SavingsAccount("AC0302", "D", 5000, 0);
        System.out.println("Lock order by account number:");
        deadlocked = runTransferPair(c, d, true);
        System.out.println("  completed: " + !deadlocked + ", balances " + c.getAccountNumber()
                + "=" + c.getBalance() + " " + d.getAccountNumber() + "=" + d.getBalance());
    }

    /**
     * Runs transfer x->y (1000) and y->x (500) at the same time.
     * @return true when the two threads deadlocked
     */
    private static boolean runTransferPair(Account x, Account y, boolean ordered)
            throws InterruptedException {
        Thread t1 = new Thread(new TransferTask(x, y, 1000, ordered), "transfer-1");
        Thread t2 = new Thread(new TransferTask(y, x, 500, ordered), "transfer-2");
        t1.setDaemon(true);           // a deadlocked daemon thread does not stop the JVM exiting
        t2.setDaemon(true);
        t1.start();
        t2.start();
        t1.join(2000);
        t2.join(2000);
        if (t1.isAlive() || t2.isAlive()) {
            // supplementary: a transfer still running after 2 s is logged as a warning
            System.out.println("  WARNING: transfer still running after 2 seconds");
            long[] ids = ManagementFactory.getThreadMXBean().findDeadlockedThreads();
            return ids != null && ids.length > 0;
        }
        return false;
    }

    /** Looks an account up by number. */
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
