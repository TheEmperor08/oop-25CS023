import java.util.Objects;

/**
 * A bank account. The balance is private and can change only through
 * deposit() and withdraw(); there is deliberately no public setter for it.
 * Practical 03: readable toString(), equality by account number, statement text.
 */
public class Account {

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

    /**
     * Creates an account whose number is already known (for example when the
     * same account is looked up or reloaded).
     */
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

    /** Adds amount to the balance. Negative or zero amounts are rejected. */
    public void deposit(long amount) {
        if (amount <= 0) {
            System.out.println("Rejected deposit of " + amount + " (amount must be positive)");
            return;
        }
        balance += amount;
    }

    /**
     * Subtracts amount when the balance is sufficient.
     * @return true on success, false (balance unchanged) otherwise
     */
    public boolean withdraw(long amount) {
        if (amount <= 0 || amount > balance) {
            return false;
        }
        balance -= amount;
        return true;
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

    /** Supplementary: formatted, multi-line statement for this account. */
    public String getStatement() {
        return "----- Account Statement -----\n"
                + "Account no : " + accountNumber + "\n"
                + "Owner      : " + ownerName + "\n"
                + "Balance    : Rs. " + balance + "\n"
                + "Status     : " + (active ? "Active" : "Inactive") + "\n"
                + "-----------------------------";
    }
}
