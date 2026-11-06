/**
 * Practical 14 - Part A - Program 1: Assemble and test (model layer).
 * One to-do task. A record is read-only, so completing a task creates a new Task.
 */
public record Task(int id, String title, boolean done) {

    @Override
    public String toString() {
        return id + ". [" + (done ? "x" : " ") + "] " + title;
    }
}
