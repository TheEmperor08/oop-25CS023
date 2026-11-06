import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Practical 11 - Part A - Program 3 (For Advanced Learners): Folder tree report.
 * Walks a folder tree with NIO (Files.walkFileTree) and reports every file's
 * name, size and last-modified time, then writes the summary to a report file.
 * Report contents chosen: relative path, size, modified time, folder count,
 * file count, total bytes and the largest file.
 */
public class FolderReport {

    private static final DateTimeFormatter TIME =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /** Creates a small sample tree so the report is reproducible. */
    static void createSampleTree(Path root) throws IOException {
        Files.createDirectories(root.resolve("docs"));
        Files.createDirectories(root.resolve("src/util"));
        Files.writeString(root.resolve("readme.txt"), "Sample project\n");
        Files.writeString(root.resolve("docs/notes.txt"),
                "Lab notes for practical 11\n".repeat(4));
        Files.writeString(root.resolve("src/Main.java"), "class Main { }\n");
        Files.writeString(root.resolve("src/util/Helper.java"),
                "class Helper {\n    static int twice(int x) { return 2 * x; }\n}\n");
    }

    public static void main(String[] args) throws IOException {
        Path root = Paths.get("sample-tree");
        createSampleTree(root);

        List<String> report = new ArrayList<>();
        long[] totals = new long[3];                // [0]=folders, [1]=files, [2]=bytes
        String[] largest = {"", "-1"};

        report.add(String.format("%-24s %8s  %s", "File", "Bytes", "Last modified"));
        Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                totals[0]++;
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                String name = root.relativize(file).toString().replace('\\', '/');
                LocalDateTime modified = LocalDateTime.ofInstant(
                        attrs.lastModifiedTime().toInstant(), ZoneId.systemDefault());
                report.add(String.format("%-24s %8d  %s",
                        name, attrs.size(), TIME.format(modified)));
                totals[1]++;
                totals[2] += attrs.size();
                if (attrs.size() > Long.parseLong(largest[1])) {
                    largest[0] = name;
                    largest[1] = String.valueOf(attrs.size());
                }
                return FileVisitResult.CONTINUE;
            }
        });
        report.add("Folders: " + totals[0] + ", files: " + totals[1]
                + ", total bytes: " + totals[2]);
        report.add("Largest file: " + largest[0] + " (" + largest[1] + " bytes)");

        Path out = Paths.get("folder-report.txt");
        Files.write(out, report);
        report.forEach(System.out::println);
        System.out.println("Report written to " + out
                + " (" + Files.size(out) + " bytes)");
    }
}
