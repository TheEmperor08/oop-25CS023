package model;

import java.time.LocalDate;

/**
 * Fixed deposit: earns 7% a year; the money is locked until the maturity date.
 * Supplementary: after maturity, withdrawal of up to the balance is allowed.
 */
public class FixedDepositAccount extends Account {

    private final LocalDate maturityDate;

    /** A one-year fixed deposit starting today. */
    public FixedDepositAccount(String ownerName, long openingBalance) {
        this(ownerName, openingBalance, LocalDate.now().plusYears(1));
    }

    public FixedDepositAccount(String ownerName, long openingBalance, LocalDate maturityDate) {
        super(ownerName, openingBalance);
        this.maturityDate = maturityDate;
    }

    public FixedDepositAccount(String accountNumber, String ownerName, long balance,
                               LocalDate maturityDate) {
        super(accountNumber, ownerName, balance);
        this.maturityDate = maturityDate;
    }

    public double interestRate() {
        return 7.0;
    }

    /** Locked (false) before maturity; afterwards up to the full balance. */
    public boolean canWithdraw(long amount) {
        boolean matured = !LocalDate.now().isBefore(maturityDate);
        return matured && amount <= getBalance();
    }

    public LocalDate getMaturityDate() {
        return maturityDate;
    }
}
