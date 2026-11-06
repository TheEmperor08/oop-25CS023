package model;

/**
 * Savings account: earns 4% a year and must keep a minimum balance.
 */
public class SavingsAccount extends Account {

    private long minBalance;

    public SavingsAccount(String ownerName, long openingBalance, long minBalance) {
        super(ownerName, openingBalance);          // initialise the inherited fields
        this.minBalance = minBalance;
    }

    public SavingsAccount(String accountNumber, String ownerName, long balance, long minBalance) {
        super(accountNumber, ownerName, balance);
        this.minBalance = minBalance;
    }

    @Override
    public double interestRate() {
        return 4.0;
    }

    /** Allowed only if the balance stays greater than or equal to minBalance. */
    @Override
    public boolean canWithdraw(long amount) {
        return amount <= availableToWithdraw();
    }

    /** Everything above the minimum balance. */
    @Override
    public long availableToWithdraw() {
        return getBalance() - minBalance;
    }

    public long getMinBalance() {
        return minBalance;
    }
}
