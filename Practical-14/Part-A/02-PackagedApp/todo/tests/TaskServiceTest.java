package todo.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import todo.model.Task;
import todo.service.TaskService;

/**
 * Practical 14 - Part A - Program 2 (For Advanced Learners): JUnit 5 tests,
 * including a save-and-reload test. Kept in its own package so the runnable
 * JAR contains only model, service and ui.
 */
class TaskServiceTest {

    @Test
    void addingATaskIncreasesTheCount() {
        TaskService service = new TaskService();
        service.add("Buy milk");
        assertEquals(1, service.count());
    }

    @Test
    void invalidActionsThrow() {
        TaskService service = new TaskService();
        assertThrows(IllegalArgumentException.class, () -> service.add(""));
        assertThrows(IllegalArgumentException.class, () -> service.complete(7));
    }

    @Test
    void savedDataReloadsUnchanged(@TempDir Path dir) throws IOException {
        TaskService service = new TaskService();
        service.add("Write README");
        service.add("Build JAR");
        service.complete(2);
        Path file = dir.resolve("tasks.dat");

        service.save(file);
        TaskService reloaded = TaskService.load(file);

        assertEquals(service.list(), reloaded.list());           // records compare by value
        Task next = reloaded.add("Next task");
        assertEquals(3, next.id());                               // id counter restored too
    }
}
