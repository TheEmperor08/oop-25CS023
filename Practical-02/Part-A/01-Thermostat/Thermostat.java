/**
 * Practical 02 - Part A - Program 1: Smart thermostat.
 * Demonstrates private fields, static constants and counters, constructor
 * chaining with this(...), and methods that keep the state within limits.
 */
public class Thermostat {

    private static final int MIN = 16;
    private static final int MAX = 30;
    private static int activeCount = 0;

    private String location;
    private int temperature;

    /** Creates a thermostat; an out-of-range start temperature falls back to 22. */
    public Thermostat(String location, int startTemp) {
        this.location = location;
        if (startTemp >= MIN && startTemp <= MAX) {
            this.temperature = startTemp;
        } else {
            this.temperature = 22;
        }
        activeCount++;
    }

    /** Creates a thermostat at the default temperature 22. */
    public Thermostat(String location) {
        this(location, 22);
    }

    public void raise() {
        if (temperature < MAX) {
            temperature++;
        } else {
            System.out.println("Already at maximum (" + MAX + ")");
        }
    }

    public void lower() {
        if (temperature > MIN) {
            temperature--;
        } else {
            System.out.println("Already at minimum (" + MIN + ")");
        }
    }

    public int getTemperature() {
        return temperature;
    }

    public String getLocation() {
        return location;
    }

    public static int getActiveCount() {
        return activeCount;
    }

    public static void main(String[] args) {
        Thermostat hall = new Thermostat("Hall");
        Thermostat bedroom = new Thermostat("Bedroom", 45);   // invalid -> 22
        System.out.println(hall.getLocation() + " starts at " + hall.getTemperature());
        System.out.println(bedroom.getLocation() + " requested 45, starts at "
                + bedroom.getTemperature());

        System.out.println("-- raise() x10 on " + hall.getLocation() + " --");
        for (int i = 1; i <= 10; i++) {
            hall.raise();
            System.out.println("raise " + i + ": " + hall.getTemperature());
        }
        System.out.println("-- lower() x20 on " + hall.getLocation() + " --");
        for (int i = 1; i <= 20; i++) {
            hall.lower();
            System.out.println("lower " + i + ": " + hall.getTemperature());
        }
        System.out.println("Active thermostats: " + Thermostat.getActiveCount());
    }
}
