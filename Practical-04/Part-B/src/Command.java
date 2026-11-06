/**
 * One parsed command line such as "DEPOSIT AC0001 500".
 * A record: immutable data with generated constructor and accessors.
 */
public record Command(TransactionType type, String accountNumber, long amount) { }
