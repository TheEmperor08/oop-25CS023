import java.util.Scanner;

/**
 * Practical 14 - Part A - Program 1: Assemble and test (user-interface layer).
 * A menu-driven main that only talks to the TaskService.
 */
public class TaskApp {

    public static void main(String[] args) {
        TaskService service = new TaskService();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.print("1. Add task  2. List tasks  3. Complete task  4. Exit > ");
            if (!scanner.hasNextLine()) {
                break;
            }
            String choice = scanner.nextLine().trim();
            try {
                switch (choice) {
                    case "1" -> {
                        System.out.print("Title: ");
                        Task t = service.add(scanner.nextLine());
                        System.out.println("Added " + t + " (total " + service.count() + ")");
                    }
                    case "2" -> {
                        if (service.count() == 0) {
                            System.out.println("No tasks yet.");
                        }
                        service.list().forEach(t -> System.out.println("  " + t));
                    }
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
        System.out.println("Goodbye!");
        scanner.close();
    }
}
