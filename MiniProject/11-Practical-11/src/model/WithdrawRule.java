package model;

/**
 * Functional interface (exactly one abstract method): a rule that decides
 * whether an account may withdraw an amount. Can be implemented by an
 * anonymous class or by a lambda expression.
 */
@FunctionalInterface
public interface WithdrawRule {

    boolean allow(Account account, long amount);
}
