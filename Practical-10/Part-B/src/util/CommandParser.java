package util;

import static util.Validator.isValidAmount;

import model.Command;
import model.TransactionType;

/**
 * Turns a typed line such as "DEPOSIT AC0001 500" into a Command object.
 */
public class CommandParser {

    private CommandParser() {
        // utility class: no objects
    }

    /**
     * Splits the line on whitespace and converts the three parts.
     * @throws IllegalArgumentException (supplementary) when the line has the wrong
     *         number of parts, an unknown type or an invalid amount
     */
    public static Command parse(String line) {
        if (line == null || line.isBlank()) {
            throw new IllegalArgumentException("Empty command");
        }
        String[] parts = line.trim().split("\\s+");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Expected 3 parts (TYPE ACCOUNT AMOUNT) but got "
                    + parts.length + " in \"" + line + "\"");
        }
        TransactionType type;
        try {
            type = TransactionType.valueOf(parts[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown transaction type: " + parts[0]);
        }
        if (!isValidAmount(parts[2])) {          // static import
            throw new IllegalArgumentException("Invalid amount: " + parts[2]);
        }
        return new Command(type, parts[1].toUpperCase(), Long.parseLong(parts[2]));
    }
}
