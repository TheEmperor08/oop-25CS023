/**
 * Practical 06 - Part A - Program 1: Remote control.
 * A ceiling fan that implements Switchable.
 */
public class Fan implements Switchable {

    private final String room;
    private boolean running;

    public Fan(String room) {
        this.room = room;
    }

    @Override
    public void on() {
        running = true;
        System.out.println("Fan in " + room + " is spinning");
    }

    @Override
    public void off() {
        running = false;
        System.out.println("Fan in " + room + " stopped");
    }

    @Override
    public boolean isOn() {
        return running;
    }

    @Override
    public String toString() {
        return "Fan(" + room + ")";
    }
}
