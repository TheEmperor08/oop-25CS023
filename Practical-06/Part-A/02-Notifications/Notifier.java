/**
 * Practical 06 - Part A - Program 2: Notification senders.
 * Functional interface with a single abstract method send(String).
 */
@FunctionalInterface
public interface Notifier {

    void send(String message);
}
