package todo.ui;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import todo.model.Task;
import todo.service.TaskService;

/**
 * Practical 14 - Part A - Program 2 (For Advanced Learners): user-interface package.
 * Main class of the runnable JAR. Loads tasks.dat at start-up and saves on exit.
 */
public class TaskApp {

    private static final Path DATA_FILE = Paths.get("tasks.dat");

    public static void main(String[] args) throws IOException {
        TaskService service;
        if (Files.exists(DATA_FILE)) {
            service = TaskService.load(DATA_FILE);
            System.out.println("Loaded " + service.count() + " tasks from " + DATA_FILE);
        } else {
            service = new TaskService();
            System.out.println("No saved data, starting empty");
        }

        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while (running) {
            System.out.print(
                    "1. Add task  2. List tasks  3. Complete task  4. Save and exit > ");
            if (!scanner.hasNextLine()) {
                break;
            }
            String choice = scanner.nextLine().trim();
            try {
                switch (choice) {
                    case "1" -> {
                        System.out.print("Title: ");
                        Task t = service.add(scanner.nextLine());
                        System.out.println("Added " + t);
                    }
                    case "2" -> service.list().forEach(t -> System.out.println("  " + t));
                    case "3" -> {
                        System.out.print("Task id: ");
                        int id = Integer.parseInt(scanner.nextLine().trim());
                        System.out.println("Completed " + service.complete(id));
                    }
                    case "4" -> running = false;
                    default -> System.out.println("Invalid choice: " + choice);
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: please enter a number");
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        service.save(DATA_FILE);
        System.out.println("Saved " + service.count() + " tasks to " + DATA_FILE
                + ". Goodbye!");
        scanner.close();
    }
}
