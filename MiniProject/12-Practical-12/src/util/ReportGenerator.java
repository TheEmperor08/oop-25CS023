package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * End-of-day report: walks the logs folder with NIO, reads every log file line by
 * line and totals the deposits and withdrawals, then writes a summary file.
 */
public class ReportGenerator {

    private static final DateTimeFormatter TIME =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private ReportGenerator() {
        // utility class: no objects
    }

    /**
     * Builds the report for every *.log file in logsDir, writes it to reportFile
     * and returns the same text.
     */
    public static String generate(Path logsDir, Path reportFile) throws IOException {
        long totalDeposits = 0;
        long totalWithdrawals = 0;
        long totalTransfers = 0;
        int lines = 0;
        StringBuilder files = new StringBuilder();

        // Supplementary: the "*.log" filter skips files that do not end with .log
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(logsDir, "*.log")) {
            for (Path file : stream) {
                BasicFileAttributes attrs = Files.readAttributes(file, BasicFileAttributes.class);
                if (attrs.size() == 0) {                   // supplementary: skip empty files
                    files.append(String.format("  %-14s skipped (empty)%n", file.getFileName()));
                    continue;
                }
                files.append(String.format("  %-14s %5d bytes, modified %s%n", file.getFileName(),
                        attrs.size(), TIME.format(LocalDateTime.ofInstant(
                                attrs.lastModifiedTime().toInstant(), ZoneId.systemDefault()))));
                try (BufferedReader reader =
                        Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.trim().split("\\s+");
                        if (parts.length < 3) {
                            continue;                      // ignore blank or malformed lines
                        }
                        long amount = Long.parseLong(parts[parts.length - 1]);
                        lines++;
                        switch (parts[0]) {               // the first word decides the total
                            case "DEPOSIT" -> totalDeposits += amount;
                            case "WITHDRAW" -> totalWithdrawals += amount;
                            case "TRANSFER" -> totalTransfers += amount;
                            default -> lines--;
                        }
                    }
                }
            }
        }

        StringBuilder report = new StringBuilder();
        report.append("===== MiniBank End-of-Day Report =====").append(System.lineSeparator());
        report.append("Log files:").append(System.lineSeparator()).append(files);
        report.append(String.format("Transactions read : %d%n", lines));
        report.append(String.format("Total deposits    : %d%n", totalDeposits));
        report.append(String.format("Total withdrawals : %d%n", totalWithdrawals));
        report.append(String.format("Total transfers   : %d (between accounts)%n", totalTransfers));
        report.append(String.format("Net change        : %d%n", totalDeposits - totalWithdrawals));
        report.append("======================================");

        if (reportFile.getParent() != null) {
            Files.createDirectories(reportFile.getParent());
        }
        Files.writeString(reportFile, report.toString() + System.lineSeparator());
        return report.toString();
    }
}
