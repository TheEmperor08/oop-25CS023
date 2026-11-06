import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Scanner;

/**
 * Practical 11 - Part A - Program 2: Log analyzer.
 * Creates a few text log files, reads each one line by line with Path/Files,
 * counts all lines and the lines containing a keyword, and prints each file's size.
 */
public class LogAnalyzer {

    /** Writes three small sample log files into the given folder. */
    static Path[] createSampleLogs(Path folder) throws IOException {
        Files.createDirectories(folder);
        Path a = Files.write(folder.resolve("server.log"), List.of(
                "09:00 INFO Server started",
                "09:05 ERROR Disk almost full",
                "09:10 INFO User riya logged in",
                "09:15 ERROR Connection timeout"));
        Path b = Files.write(folder.resolve("payments.log"), List.of(
                "10:00 INFO Payment 501 accepted",
                "10:02 WARN Payment 502 retried",
                "10:03 ERROR Payment 503 declined"));
        Path c = Files.write(folder.resolve("audit.log"), List.of(
                "11:00 INFO Report generated",
                "11:30 INFO Backup complete"));
        return new Path[] {a, b, c};
    }

    public static void main(String[] args) throws IOException {
        Path folder = Paths.get("sample-logs");            // inside the working directory
        Path[] files = createSampleLogs(folder);

        Scanner scanner = new Scanner(System.in);
        System.out.print("Keyword to search for: ");
        String keyword = scanner.hasNextLine() ? scanner.nextLine().trim() : "ERROR";
        String needle = keyword.toLowerCase();

        int totalLines = 0;
        int totalMatches = 0;
        for (Path file : files) {
            int lines = 0;
            int matches = 0;
            try (BufferedReader reader = Files.newBufferedReader(file)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    lines++;
                    if (line.toLowerCase().contains(needle)) {
                        matches++;
                    }
                }
            }
            BasicFileAttributes attrs = Files.readAttributes(file, BasicFileAttributes.class);
            System.out.printf("%-14s lines=%d  matches=%d  size=%d bytes%n",
                    file.getFileName(), lines, matches, attrs.size());
            totalLines += lines;
            totalMatches += matches;
        }
        System.out.println("Total lines: " + totalLines);
        System.out.println("Lines containing '" + keyword + "': " + totalMatches);
        scanner.close();
    }
}
