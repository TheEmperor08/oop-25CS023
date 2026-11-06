import java.util.Scanner;

/**
 * Practical 01 - Part A - Program 2: Toll booth.
 * Reads vehicles until "done", charges a toll per type and reports the totals.
 */
public class TollBooth {

    /** A vehicle passing the booth (read-only data). */
    record Vehicle(String number, String type) { }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        long totalToll = 0;
        int bikeCount = 0;
        int carCount = 0;
        int truckCount = 0;

        while (true) {
            System.out.print("Vehicle number (or done): ");
            if (!scanner.hasNextLine()) {
                break;
            }
            String number = scanner.nextLine().trim();
            if (number.equalsIgnoreCase("done")) {
                break;
            }
            System.out.print("Type (bike/car/truck): ");
            if (!scanner.hasNextLine()) {
                break;
            }
            Vehicle vehicle = new Vehicle(number, scanner.nextLine().trim().toLowerCase());

            int toll = switch (vehicle.type()) {
                case "bike" -> 20;
                case "car" -> 50;
                case "truck" -> 150;
                default -> -1;
            };
            if (toll < 0) {
                System.out.println("Unknown type '" + vehicle.type() + "', vehicle ignored.");
                continue;
            }
            totalToll += toll;
            switch (vehicle.type()) {
                case "bike" -> bikeCount++;
                case "car" -> carCount++;
                default -> truckCount++;
            }
            System.out.println(vehicle + " charged " + toll);
        }

        System.out.println("Total toll: " + totalToll);
        if (bikeCount + carCount + truckCount == 0) {
            System.out.println("No vehicles passed.");
        } else {
            String mostFrequent = "bike";
            int best = bikeCount;
            if (carCount > best) {
                mostFrequent = "car";
                best = carCount;
            }
            if (truckCount > best) {
                mostFrequent = "truck";
            }
            System.out.println("Most frequent: " + mostFrequent);
        }
        scanner.close();
    }
}
