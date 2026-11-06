/**
 * Read-only bank details shown as the application header.
 * A record auto-generates the constructor, the accessors name() and branch(),
 * equals(), hashCode() and toString().
 */
public record BankInfo(String name, String branch) { }
