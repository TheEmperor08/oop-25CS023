package exception;

/** Thrown when no account exists with the given number. */
public class AccountNotFoundException extends BankException {

    private static final long serialVersionUID = 1L;

    public AccountNotFoundException(String accountNumber) {
        super("Account not found: " + accountNumber);
    }
}
