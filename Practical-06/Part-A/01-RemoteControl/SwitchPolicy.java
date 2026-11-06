/**
 * Practical 06 - Part A - Program 1: Remote control.
 * Functional interface: decides whether a device may be switched on at a given hour.
 */
@FunctionalInterface
public interface SwitchPolicy {

    boolean mayTurnOn(Switchable device, int hour);
}
