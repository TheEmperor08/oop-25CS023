import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Practical 14 - Part A - Program 1: Assemble and test (JUnit 5 tests).
 */
class TaskServiceTest {

    @Test
    void addingATaskIncreasesTheCount() {
        TaskService service = new TaskService();
        service.add("Buy milk");
        service.add("Submit lab");
        assertEquals(2, service.count());
    }

    @Test
    void addingABlankTitleThrows() {
        TaskService service = new TaskService();
        assertThrows(IllegalArgumentException.class, () -> service.add("   "));
        assertEquals(0, service.count());
    }

    @Test
    void completingAnUnknownIdThrows() {
        TaskService service = new TaskService();
        service.add("Read chapter 5");
        assertThrows(IllegalArgumentException.class, () -> service.complete(99));
        assertTrue(service.complete(1).done());
    }
}
