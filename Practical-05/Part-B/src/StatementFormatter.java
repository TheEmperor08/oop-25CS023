/**
 * Builds a multi-line account statement with a StringBuilder
 * (efficient for many appends, unlike repeated String concatenation).
 */
public class StatementFormatter {

    private StatementFormatter() {
        // utility class: no objects
    }

    public static String buildStatement(Account account) {
        StringBuilder sb = new StringBuilder();
        sb.append("=================================\n");
        sb.append("        MINIBANK STATEMENT\n");
        sb.append("=================================\n");
        sb.append(String.format("Account no : %s%n", account.getAccountNumber()));
        sb.append(String.format("Owner      : %s%n", account.getOwnerName()));
        sb.append(String.format("Status     : %s%n", account.isActive() ? "Active" : "Inactive"));
        sb.append(String.format("Balance    : Rs. %,d%n", account.getBalance()));
        sb.append("=================================");
        return sb.toString();
    }
}
