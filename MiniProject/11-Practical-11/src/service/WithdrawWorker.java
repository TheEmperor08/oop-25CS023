package service;

import exception.BankException;
import model.Account;

/**
 * Supplementary: a task that withdraws the same amount from one shared account
 * a number of times (runs alongside AccountWorker deposit threads).
 */
public class WithdrawWorker implements Runnable {

    private final Account account;
    private final int times;
    private final long amount;

    public WithdrawWorker(Account account, int times, long amount) {
        this.account = account;
        this.times = times;
        this.amount = amount;
    }

    @Override
    public void run() {
        for (int i = 0; i < times; i++) {
            try {
                account.withdraw(amount);
            } catch (BankException e) {
                System.out.println(Thread.currentThread().getName() + ": " + e.getMessage());
                return;
            }
        }
    }
}
