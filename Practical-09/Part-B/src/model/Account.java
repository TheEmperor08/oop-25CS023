package model;

import exception.AccountNotFoundException;
import exception.BankException;
import exception.DailyLimitExceededException;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import java.time.LocalDate;
import java.util.Objects;
import model.annotation.Id;
import model.annotation.MaxLength;
import model.annotation.Positive;

/**
 * Abstract base for every MiniBank account type. The balance is private and can
 * change only through deposit() and withdraw(); there is no public setter.
 * Practical 05: abstract class; each subclass supplies its own interest and
 * withdrawal rules, chosen at run time by polymorphism.
 * Practical 06: implements Transactable and InterestBearing.
 * Practical 07: @Id, @MaxLength and @Positive field annotations, read by reflection.
 * Practical 08: deposit/withdraw/transfer report failures with checked exceptions.
 * Practical 09: deposit, withdraw and getBalance are synchronized (thread-safe).
 */
public abstract class Account implements Transactable, InterestBearing {

    /** Supplementary (Practical 08): the most that may be withdrawn in one day. */
    public static final long DAILY_WITHDRAWAL_LIMIT = 50_000;

    /** Shared counter used to generate account numbers AC0001, AC0002, ... */
    private static long accountCounter = 0;

    @Id
    private final String accountNumber;
    @MaxLength(30)
    private String ownerName;
    @Positive
    private long balance;          // whole rupees
    private boolean active;
    private long withdrawnToday;   // running total for the daily limit
    private LocalDate withdrawalDay;

    /** Opens an account with an opening balance. */
    public Account(String ownerName, long openingBalance) {
        this(generateAccountNumber(), ownerName, openingBalance);
    }

    /** Opens an account with a zero balance (constructor chaining). */
    public Account(String ownerName) {
        this(ownerName, 0);
    }

    /** Creates an account whose number is already known (e.g. looked up or reloaded). */
    public Account(String accountNumber, String ownerName, long balance) {
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.balance = balance;
        this.active = true;
        reserveNumber(accountNumber);
    }

    private static String generateAccountNumber() {
        accountCounter++;
        return String.format("AC%04d", accountCounter);
    }

    /** Keeps the counter ahead of any number supplied explicitly, so ids never clash. */
    private static void reserveNumber(String accountNumber) {
        if (accountNumber != null && accountNumber.matches("AC\\d+")) {
            long used = Long.parseLong(accountNumber.substring(2));
            if (used > accountCounter) {
                accountCounter = used;
            }
        }
    }

    /** Yearly interest rate in percent for this type of account. */
    public abstract double interestRate();

    /** Whether this account type allows withdrawing the given amount now. */
    public abstract boolean canWithdraw(long amount);

    /** The most this account type lets the owner withdraw right now. */
    public abstract long availableToWithdraw();

    /**
     * Adds amount to the balance.
     * @throws InvalidAmountException when amount is zero or negative
     */
    @Override
    public synchronized void deposit(long amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException(amount);
        }
        balance += amount;
    }

    /**
     * Subtracts amount when the account type's rule (canWithdraw) allows it.
     * @throws InvalidAmountException      amount is zero or negative
     * @throws DailyLimitExceededException today's withdrawals would pass the daily cap
     * @throws InsufficientFundsException  the account cannot give that much; the
     *                                     exception carries the shortfall
     */
    @Override
    public synchronized void withdraw(long amount)
            throws InsufficientFundsException, InvalidAmountException, DailyLimitExceededException {
        if (amount <= 0) {
            throw new InvalidAmountException(amount);
        }
        if (!LocalDate.now().equals(withdrawalDay)) {     // a new day: reset the running total
            withdrawalDay = LocalDate.now();
            withdrawnToday = 0;
        }
        if (withdrawnToday + amount > DAILY_WITHDRAWAL_LIMIT) {
            throw new DailyLimitExceededException(DAILY_WITHDRAWAL_LIMIT, withdrawnToday, amount);
        }
        if (!canWithdraw(amount)) {
            throw new InsufficientFundsException(accountNumber,
                    amount - Math.max(0, availableToWithdraw()));
        }
        balance -= amount;
        withdrawnToday += amount;
    }

    /**
     * Moves amount from this account to another one. If any step fails the
     * withdrawal is rolled back and the BankException is re-thrown to the caller.
     */
    public void transfer(Account to, long amount) throws BankException {
        boolean withdrawn = false;
        try {
            if (to == null) {
                throw new AccountNotFoundException("(null)");
            }
            withdraw(amount);
            withdrawn = true;
            to.deposit(amount);
        } catch (BankException e) {
            if (withdrawn) {                                 // undo the half-done transfer
                undoWithdraw(amount);
            }
            throw e;                                         // re-throw to the caller
        } finally {
            System.out.println("  [transfer log] " + accountNumber + " -> "
                    + (to == null ? "?" : to.accountNumber) + " amount " + amount);
        }
    }

    /** Supplementary: one month's interest, using the subclass's interestRate(). */
    public double monthlyInterest() {
        return balance * interestRate() / 100.0 / 12.0;
    }

    /** Puts back money taken by a withdrawal that could not be completed. */
    private synchronized void undoWithdraw(long amount) {
        balance += amount;
        withdrawnToday -= amount;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    @Override
    public synchronized long getBalance() {
        return balance;
    }

    public boolean isActive() {
        return active;
    }

    /** Readable one-line form, e.g. "AC0001 | Riya | 4000 | active". */
    @Override
    public String toString() {
        return accountNumber + " | " + ownerName + " | " + balance + " | "
                + (active ? "active" : "inactive");
    }

    /** Two accounts are equal when their account numbers are equal. */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Account)) {
            return false;
        }
        Account other = (Account) o;
        return accountNumber.equals(other.accountNumber);
    }

    /** Consistent with equals(): based only on the account number. */
    @Override
    public int hashCode() {
        return Objects.hash(accountNumber);
    }

    /** Formatted, multi-line statement for this account. */
    public String getStatement() {
        return "----- Account Statement -----\n"
                + "Account no : " + accountNumber + "\n"
                + "Owner      : " + ownerName + "\n"
                + "Balance    : Rs. " + balance + "\n"
                + "Status     : " + (active ? "Active" : "Inactive") + "\n"
                + "-----------------------------";
    }
}
