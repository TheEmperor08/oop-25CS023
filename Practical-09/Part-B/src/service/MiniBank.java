package service;

import java.util.Scanner;
import java.util.concurrent.atomic.AtomicLong;
import model.Account;
import model.BankInfo;
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
 * Practical 09: AccountWorker threads, race condition, synchronized deposit/withdraw.
 */
public class MiniBank {

    public static void main(String[] args) {
        BankInfo bank = new BankInfo("MiniBank", "Changa Branch");
        System.out.println("=== " + bank.name() + " - " + bank.branch() + " ===");
        System.out.println(bank);

        runPracticalDemo();
        runMenu(bank);
    }

    /** Practical 09 demo: many threads depositing into one account. */
    private static void runPracticalDemo() {
        System.out.println();
        System.out.println("--- Practical 09: Threads and synchronization ---");
        System.out.println("10 threads x 1000 deposits of 1 into one account (expected 10000)");
        for (int round = 1; round <= 3; round++) {
            Account account = new SavingsAccount("Shared", 0, 0);
            runThreads(account, 10, 1000, round == 1);
            System.out.println("Round " + round + ": final balance = " + account.getBalance());
        }

        // Supplementary 1: the same experiment with an AtomicLong counter
        AtomicLong atomicBalance = new AtomicLong();
        Thread[] atomicThreads = new Thread[10];
        for (int i = 0; i < atomicThreads.length; i++) {
            atomicThreads[i] = new Thread(() -> {
                for (int k = 0; k < 1000; k++) {
                    atomicBalance.addAndGet(1);
                }
            });
            atomicThreads[i].start();
        }
        joinAll(atomicThreads);
        System.out.println("AtomicLong version: final balance = " + atomicBalance.get());

        // Supplementary 2: depositing and withdrawing threads at the same time
        Account mixed = new SavingsAccount("Mixed", 10000, 0);
        Thread[] threads = new Thread[15];
        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(new AccountWorker(mixed, 1000, 1), "depositor-" + (i + 1));
        }
        for (int i = 10; i < 15; i++) {
            threads[i] = new Thread(new WithdrawWorker(mixed, 1000, 1), "withdrawer-" + (i - 9));
        }
        for (Thread t : threads) {
            t.start();
        }
        joinAll(threads);
        System.out.println("10 depositors + 5 withdrawers on 10000: final balance = "
                + mixed.getBalance() + " (expected 15000)");
    }

    /**
     * Starts the given number of AccountWorker threads on one account and waits
     * for all of them with join(). Optionally prints each thread's life cycle.
     */
    private static void runThreads(Account account, int count, int times, boolean verbose) {
        Thread[] threads = new Thread[count];
        Thread.State[] before = new Thread.State[count];
        Thread.State[] running = new Thread.State[count];
        for (int i = 0; i < count; i++) {
            threads[i] = new Thread(new AccountWorker(account, times, 1), "worker-" + (i + 1));
            before[i] = threads[i].getState();                 // NEW
        }
        for (int i = 0; i < count; i++) {
            threads[i].start();
            running[i] = threads[i].getState();                // RUNNABLE / BLOCKED / ...
        }
        joinAll(threads);
        if (verbose) {
            for (int i = 0; i < count; i++) {
                System.out.println("  " + threads[i].getName() + ": " + before[i] + " -> "
                        + running[i] + " -> " + threads[i].getState());
            }
        }
    }

    /** Waits for every thread to finish. */
    private static void joinAll(Thread[] threads) {
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
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
