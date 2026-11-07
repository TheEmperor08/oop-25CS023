package service;

import exception.BankException;
import model.Account;

/**
 * Transfers money between two accounts while holding the locks of BOTH accounts.
 * With orderedLocking = false it locks "from" first, so two opposite transfers
 * (A to B and B to A) can deadlock. With orderedLocking = true it always locks
 * the account with the smaller account number first, which prevents deadlock.
 */
public class TransferTask implements Runnable {

    private final Account from;
    private final Account to;
    private final long amount;
    private final boolean orderedLocking;

    public TransferTask(Account from, Account to, long amount, boolean orderedLocking) {
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.orderedLocking = orderedLocking;
    }

    @Override
    public void run() {
        Account first = from;
        Account second = to;
        if (orderedLocking && from.getAccountNumber().compareTo(to.getAccountNumber()) > 0) {
            first = to;                    // smaller account number first
            second = from;
        }
        String name = Thread.currentThread().getName();
        synchronized (first) {
            System.out.println("  " + name + " locked " + first.getAccountNumber()
                    + ", waiting for " + second.getAccountNumber());
            pause(200);                    // give the other thread time to take its first lock
            synchronized (second) {
                try {
                    from.transfer(to, amount);
                } catch (BankException e) {
                    System.out.println("  " + name + " failed: " + e.getMessage());
                }
            }
        }
    }

    private static void pause(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
