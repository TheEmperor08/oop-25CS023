package todo.model;

import java.io.Serializable;

/**
 * Practical 14 - Part A - Program 2 (For Advanced Learners): model package.
 * One to-do task; Serializable so the service can save and reload it.
 */
public record Task(int id, String title, boolean done) implements Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return id + ". [" + (done ? "x" : " ") + "] " + title;
    }
}
