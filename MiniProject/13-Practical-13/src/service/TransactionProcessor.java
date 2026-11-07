package service;

import exception.AccountNotFoundException;
import exception.BankException;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import model.Account;
import model.Command;

/**
 * Runs transaction tasks on a fixed pool of 4 reusable threads instead of
 * creating a new thread for every task.
 * Practical 12: the shared accounts live in a ConcurrentHashMap, which many pool
 * threads can read and update safely at the same time.
 */
public class TransactionProcessor {

    private final ExecutorService pool = Executors.newFixedThreadPool(4);

    /** Supplementary (Practical 10): how many tasks each pool thread executed. */
    private final Map<String, AtomicInteger> tasksPerThread = new ConcurrentHashMap<>();

    /** Thread-safe shared store of the accounts the tasks work on. */
    private final ConcurrentHashMap<String, Account> accounts = new ConcurrentHashMap<>();

    /** Failed transactions, recorded by the pool threads. */
    private final AtomicInteger failures = new AtomicInteger();

    /** Hands a task to the pool; one of the 4 threads will run it. */
    public void submit(Runnable task) {
        pool.execute(() -> {
            tasksPerThread.computeIfAbsent(Thread.currentThread().getName(),
                    name -> new AtomicInteger()).incrementAndGet();
            task.run();
        });
    }

    /** Makes an account available to the tasks. */
    public void register(Account account) {
        accounts.put(account.getAccountNumber(), account);
    }

    public Account getAccount(String accountNumber) throws AccountNotFoundException {
        Account account = accounts.get(accountNumber);
        if (account == null) {
            throw new AccountNotFoundException(accountNumber);
        }
        return account;
    }

    /** Submits one parsed command (DEPOSIT/WITHDRAW) to run on the pool. */
    public void submit(Command command) {
        submit(() -> {
            try {
                Account account = getAccount(command.accountNumber());
                switch (command.type()) {
                    case DEPOSIT -> account.deposit(command.amount());
                    case WITHDRAW -> account.withdraw(command.amount());
                    case TRANSFER -> throw new BankException("TRANSFER needs two accounts");
                }
            } catch (BankException e) {
                failures.incrementAndGet();
            }
        });
    }

    /** Stops accepting tasks and waits for the submitted ones to finish. */
    public void stop() throws InterruptedException {
        pool.shutdown();
        if (!pool.awaitTermination(30, TimeUnit.SECONDS)) {
            pool.shutdownNow();
        }
    }

    /** Thread name -> number of tasks it ran (sorted by name). */
    public Map<String, Integer> getTasksPerThread() {
        Map<String, Integer> result = new TreeMap<>();
        tasksPerThread.forEach((name, count) -> result.put(name, count.get()));
        return result;
    }

    public int getFailures() {
        return failures.get();
    }
}
