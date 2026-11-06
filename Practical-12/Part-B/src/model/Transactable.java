package model;

import exception.DailyLimitExceededException;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;

/**
 * Capability of anything that money can be paid into and taken out of.
 * Practical 08: failures are reported with checked exceptions instead of flags.
 */
public interface Transactable {

    void deposit(long amount) throws InvalidAmountException;

    void withdraw(long amount)
            throws InsufficientFundsException, InvalidAmountException, DailyLimitExceededException;
}
