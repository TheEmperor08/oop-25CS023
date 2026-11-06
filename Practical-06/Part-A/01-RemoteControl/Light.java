/**
 * Practical 06 - Part A - Program 1: Remote control.
 * A light that implements Switchable.
 */
public class Light implements Switchable {

    private final String room;
    private boolean lit;

    public Light(String room) {
        this.room = room;
    }

    @Override
    public void on() {
        lit = true;
        System.out.println("Light in " + room + " is ON");
    }

    @Override
    public void off() {
        lit = false;
        System.out.println("Light in " + room + " is OFF");
    }

    @Override
    public boolean isOn() {
        return lit;
    }

    @Override
    public String toString() {
        return "Light(" + room + ")";
    }
}
