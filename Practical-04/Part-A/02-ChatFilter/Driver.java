import java.util.Scanner;

/**
 * Practical 04 - Part A - Program 2: Chat-log filter (driver).
 * Reads a keyword and prints the number of matching messages and a report.
 */
public class Driver {

    public static void main(String[] args) {
        String[] log = {
            "10:05 alice Hello there",
            "10:06 bob How are you?",
            "10:07 carol"                       // malformed: only 2 parts
        };

        Scanner scanner = new Scanner(System.in);
        System.out.print("Keyword: ");
        if (!scanner.hasNextLine()) {
            System.out.println("No keyword entered.");
            return;
        }
        String keyword = scanner.nextLine().trim();

        ChatFilter filter = new ChatFilter(keyword);
        filter.process(log);
        System.out.println("Matches: " + filter.getMatches());
        System.out.print(filter.getReport());
        scanner.close();
    }
}
