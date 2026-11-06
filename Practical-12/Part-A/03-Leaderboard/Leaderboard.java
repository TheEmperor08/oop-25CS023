import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Practical 12 - Part A - Program 3 (For Advanced Learners): Leaderboard.
 * Player scores from several games are totalled in a Map and the top N players
 * are printed sorted by score (highest first), ties broken by name.
 * Data chosen: points from three quiz rounds; N is read from the user.
 */
public class Leaderboard {

    private final Map<String, Integer> scores = new HashMap<>();

    /** Adds points to a player's running total. */
    void addScore(String player, int points) {
        scores.merge(player, points, Integer::sum);
    }

    /** Returns the top n entries: score descending, then name ascending. */
    List<Map.Entry<String, Integer>> top(int n) {
        Comparator<Map.Entry<String, Integer>> order =
                Map.Entry.<String, Integer>comparingByValue().reversed()
                        .thenComparing(Map.Entry.comparingByKey());
        List<Map.Entry<String, Integer>> list = new ArrayList<>(scores.entrySet());
        list.sort(order);
        return list.subList(0, Math.min(n, list.size()));
    }

    public static void main(String[] args) {
        Leaderboard board = new Leaderboard();
        String[][] rounds = {
            {"Riya:40", "Aman:35", "Neha:50", "Kabir:20", "Zoya:45", "Dev:30"},
            {"Riya:30", "Aman:45", "Neha:20", "Kabir:50", "Zoya:25", "Dev:40"},
            {"Riya:25", "Aman:10", "Neha:20", "Kabir:15", "Zoya:10", "Dev:5"}
        };
        for (String[] round : rounds) {
            for (String entry : round) {
                String[] parts = entry.split(":");
                board.addScore(parts[0], Integer.parseInt(parts[1]));
            }
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("How many top players to show? ");
        int n = 3;
        if (scanner.hasNextInt()) {
            n = Math.max(1, scanner.nextInt());
        }
        System.out.println("Top " + n + " (ties broken by name):");
        int rank = 1;
        for (Map.Entry<String, Integer> e : board.top(n)) {
            System.out.printf("  %d. %-6s %d%n", rank++, e.getKey(), e.getValue());
        }
        scanner.close();
    }
}
