package util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * Append-only transaction log: every call adds one line such as
 * "DEPOSIT AC0001 500" to the end of the file; existing lines are never changed.
 */
public class TransactionLog {

    private TransactionLog() {
        // utility class: no objects
    }

    public static void append(Path file, String line) throws IOException {
        if (file.getParent() != null) {
            Files.createDirectories(file.getParent());
        }
        byte[] bytes = (line + System.lineSeparator()).getBytes(StandardCharsets.UTF_8);
        Files.write(file, bytes, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }
}
