package model;

/**
 * Capability of anything that money can be paid into and taken out of.
 */
public interface Transactable {

    void deposit(long amount);

    boolean withdraw(long amount);
}
