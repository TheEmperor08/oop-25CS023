/**
 * A bank account. The balance is private and can change only through
 * deposit() and withdraw(); there is deliberately no public setter for it.
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
        this.accountNumber = generateAccountNumber();
        this.ownerName = ownerName;
        this.balance = openingBalance;
        this.active = true;
    }

    /** Opens an account with a zero balance (constructor chaining). */
    public Account(String ownerName) {
        this(ownerName, 0);
    }

    private static String generateAccountNumber() {
        accountCounter++;
        return String.format("AC%04d", accountCounter);
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
}
