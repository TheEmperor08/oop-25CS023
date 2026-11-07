package model;

/**
 * The fixed set of actions MiniBank offers. The menu number shown to the user
 * is the constant's position (ordinal + 1).
 * Practical 14: every option now calls a real BankService operation.
 */
public enum MenuOption {
    OPEN_ACCOUNT,
    DEPOSIT,
    WITHDRAW,
    TRANSFER,
    STATEMENT,
    LIST_ACCOUNTS,
    SEARCH_BY_OWNER,
    BATCH_PROCESS,
    REPORT,
    WORKING_HOURS,
    EXIT
}
