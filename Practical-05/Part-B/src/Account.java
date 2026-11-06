import java.util.Objects;

/**
 * Abstract base for every MiniBank account type. The balance is private and can
 * change only through deposit() and withdraw(); there is no public setter.
 * Practical 05: abstract class; each subclass supplies its own interest and
 * withdrawal rules, chosen at run time by polymorphism.
 */
public abstract class Account {

    /** Shared counter used to generate account numbers AC0001, AC0002, ... */
    private static long accountCounter = 0;

    private final String accountNumber;
    private String ownerName;
    private long balance;          // whole rupees
    private boolean active;

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

    /** Adds amount to the balance. Negative or zero amounts are rejected. */
    public void deposit(long amount) {
        if (amount <= 0) {
            System.out.println("Rejected deposit of " + amount + " (amount must be positive)");
            return;
        }
        balance += amount;
    }

    /**
     * Subtracts amount when the account type's rule (canWithdraw) allows it.
     * @return true on success, false (balance unchanged) otherwise
     */
    public boolean withdraw(long amount) {
        if (amount <= 0 || !canWithdraw(amount)) {
            return false;
        }
        balance -= amount;
        return true;
    }

    /** Supplementary: one month's interest, using the subclass's interestRate(). */
    public double monthlyInterest() {
        return balance * interestRate() / 100.0 / 12.0;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public long getBalance() {
        return balance;
    }

    public boolean isActive() {
        return active;
    }

    /** Readable one-line form, e.g. "AC0001 | Riya | 4000 | active". */
    public String toString() {
        return accountNumber + " | " + ownerName + " | " + balance + " | "
                + (active ? "active" : "inactive");
    }

    /** Two accounts are equal when their account numbers are equal. */
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
