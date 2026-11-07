package model;

/**
 * Current account: no interest, but the balance may go negative down to
 * minus the overdraft limit.
 */
public class CurrentAccount extends Account implements Premium {

    private long overdraftLimit;

    public CurrentAccount(String ownerName, long openingBalance, long overdraftLimit) {
        super(ownerName, openingBalance);
        this.overdraftLimit = overdraftLimit;
    }

    public CurrentAccount(String accountNumber, String ownerName, long balance,
                          long overdraftLimit) {
        super(accountNumber, ownerName, balance);
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public double interestRate() {
        return 0;
    }

    /** Allowed while the balance does not fall below -overdraftLimit. */
    @Override
    public boolean canWithdraw(long amount) {
        return amount <= availableToWithdraw();
    }

    /** The balance plus the overdraft that may still be used. */
    @Override
    public long availableToWithdraw() {
        return getBalance() + overdraftLimit;
    }

    public long getOverdraftLimit() {
        return overdraftLimit;
    }
}
