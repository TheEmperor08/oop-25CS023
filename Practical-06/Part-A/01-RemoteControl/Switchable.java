/**
 * Practical 06 - Part A - Program 1: Remote control.
 * A device that can be switched on and off. toggle() is a default method,
 * so every implementing class gets it for free.
 */
public interface Switchable {

    void on();

    void off();

    boolean isOn();

    /** Flips the current state using the abstract methods. */
    default void toggle() {
        if (isOn()) {
            off();
        } else {
            on();
        }
    }
}
