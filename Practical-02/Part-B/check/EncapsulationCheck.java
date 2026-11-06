/**
 * Post-lab check for Practical 02: this file is EXPECTED NOT to compile.
 * It proves that the balance of an Account cannot be modified from outside
 * the class - only deposit() and withdraw() can change it.
 */
public class EncapsulationCheck {
    public static void main(String[] args) {
        Account account = new Account("Riya", 5000);
        account.balance = 1_000_000;          // not allowed: balance is private
        account.setBalance(1_000_000);        // not allowed: there is no setter
    }
}
