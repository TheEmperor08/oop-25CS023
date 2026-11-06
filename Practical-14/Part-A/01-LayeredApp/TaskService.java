import java.util.ArrayList;
import java.util.List;

/**
 * Practical 14 - Part A - Program 1: Assemble and test (service layer).
 * Keeps the tasks in an ArrayList and enforces the rules; the menu never
 * touches the list directly.
 */
public class TaskService {
    private final List<Task> tasks = new ArrayList<>();
    private int nextId = 1;

    /** Adds a task; a blank title is an invalid action. */
    public Task add(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title must not be blank");
        }
        Task task = new Task(nextId++, title.trim(), false);
        tasks.add(task);
        return task;
    }

    /** Marks a task as done; an unknown id is an invalid action. */
    public Task complete(int id) {
        for (int i = 0; i < tasks.size(); i++) {
            Task t = tasks.get(i);
            if (t.id() == id) {
                Task doneTask = new Task(t.id(), t.title(), true);
                tasks.set(i, doneTask);
                return doneTask;
            }
        }
        throw new IllegalArgumentException("No task with id " + id);
    }

    /** Returns a read-only copy of all tasks. */
    public List<Task> list() {
        return List.copyOf(tasks);
    }

    public int count() {
        return tasks.size();
    }
}
