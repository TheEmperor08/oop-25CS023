import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Practical 12 - Part A - Program 1: Word frequency.
 * Splits a paragraph into words, counts each word in a HashMap and prints the
 * words sorted by frequency (highest first) using a Comparator.
 */
public class WordFrequency {

    public static void main(String[] args) {
        String paragraph = "Java is a language. Java runs on the JVM and the JVM runs "
                + "bytecode. A class is a blueprint and an object is an instance of a class. "
                + "Java collections store objects; a map stores keys and values.";

        Map<String, Integer> counts = new HashMap<>();
        for (String word : paragraph.toLowerCase().split("[^a-z]+")) {
            if (!word.isEmpty()) {
                counts.merge(word, 1, Integer::sum);
            }
        }

        // Highest frequency first; words with the same count in alphabetical order.
        Comparator<Map.Entry<String, Integer>> byFrequency =
                Map.Entry.<String, Integer>comparingByValue().reversed()
                        .thenComparing(Map.Entry.comparingByKey());
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(counts.entrySet());
        entries.sort(byFrequency);

        System.out.println("Distinct words: " + counts.size());
        System.out.println("Words by frequency:");
        for (Map.Entry<String, Integer> e : entries) {
            if (e.getValue() < 2) {
                break;                      // list only the repeated words in full
            }
            System.out.printf("  %-8s %d%n", e.getKey(), e.getValue());
        }
        long singles = entries.stream().filter(e -> e.getValue() == 1).count();
        System.out.println("  (" + singles + " other words appear once)");
    }
}
