/**
 * Practical 04 - Part A - Program 2: Chat-log filter.
 * Splits each log line into time, user and message and collects the
 * lines whose message contains a keyword (case-insensitive).
 */
public class ChatFilter {

    private final String keyword;
    private int matches;
    private final StringBuilder report = new StringBuilder();

    public ChatFilter(String keyword) {
        this.keyword = keyword.toLowerCase();
    }

    /** Processes every line; malformed lines (fewer than 3 parts) are skipped. */
    public void process(String[] lines) {
        for (String line : lines) {
            String[] parts = line.split(" ", 3);
            if (parts.length < 3) {
                System.out.println("Skipping malformed line: \"" + line + "\"");
                continue;
            }
            String time = parts[0];
            String user = parts[1];
            String message = parts[2];
            if (message.toLowerCase().contains(keyword)) {
                matches++;
                report.append(time).append(' ').append(user).append(": ")
                      .append(message).append('\n');
            }
        }
    }

    public int getMatches() {
        return matches;
    }

    public String getReport() {
        return report.toString();
    }
}
