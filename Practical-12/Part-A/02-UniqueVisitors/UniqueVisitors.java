import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Practical 12 - Part A - Program 2: Unique visitors.
 * Uses a HashSet to count unique logins and a LinkedHashSet to keep the order
 * in which each user was first seen.
 */
public class UniqueVisitors {

    public static void main(String[] args) {
        List<String> logins = List.of("riya", "aman", "riya", "neha", "kabir", "aman",
                "riya", "zoya", "neha", "kabir", "dev");
        System.out.println("Logins (" + logins.size() + "): " + logins);

        Set<String> unique = new HashSet<>(logins);              // no order guarantee
        Set<String> firstSeen = new LinkedHashSet<>(logins);     // insertion order kept

        System.out.println("Unique users: " + unique.size());
        System.out.println("First-seen order: " + firstSeen);

        System.out.print("First three distinct names:");
        Iterator<String> it = firstSeen.iterator();
        for (int i = 0; i < 3 && it.hasNext(); i++) {
            System.out.print(" " + it.next());
        }
        System.out.println();
    }
}
