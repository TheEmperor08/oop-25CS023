package todo.service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import todo.model.Task;

/**
 * Practical 14 - Part A - Program 2 (For Advanced Learners): service package.
 * Business rules over an ArrayList of tasks, plus saving and reloading the
 * tasks with object serialization.
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

    public List<Task> list() {
        return List.copyOf(tasks);
    }

    public int count() {
        return tasks.size();
    }

    /** Saves all tasks to the file. */
    public void save(Path file) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(file))) {
            out.writeObject(new ArrayList<>(tasks));
        }
    }

    /** Creates a service holding the tasks stored in the file. */
    public static TaskService load(Path file) throws IOException {
        TaskService service = new TaskService();
        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(file))) {
            List<?> saved = (List<?>) in.readObject();
            for (Object o : saved) {
                Task t = (Task) o;
                service.tasks.add(t);
                service.nextId = Math.max(service.nextId, t.id() + 1);
            }
        } catch (ClassNotFoundException | ClassCastException e) {
            throw new IOException("Not a task file: " + file, e);
        }
        return service;
    }
}
