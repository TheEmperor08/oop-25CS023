/**
 * Practical 06 - Part A - Program 1: Remote control.
 * Toggles a Switchable[] through the default toggle() method, then applies a
 * SwitchPolicy supplied once as an anonymous class and once as a lambda.
 */
public class RemoteControl {

    public static void main(String[] args) {
        Switchable[] devices = {new Fan("Hall"), new Light("Hall"), new Light("Bedroom")};

        System.out.println("--- Toggle every device (all start OFF) ---");
        for (Switchable device : devices) {
            device.toggle();
        }
        System.out.println("--- Toggle again ---");
        for (Switchable device : devices) {
            device.toggle();
        }

        // 1) Policy as an anonymous class: lights only between 18:00 and 23:00.
        SwitchPolicy eveningLights = new SwitchPolicy() {
            @Override
            public boolean mayTurnOn(Switchable device, int hour) {
                return !(device instanceof Light) || (hour >= 18 && hour <= 23);
            }
        };

        // 2) Policy as a lambda: fans only between 10:00 and 22:00.
        SwitchPolicy daytimeFans = (device, hour) ->
                !(device instanceof Fan) || (hour >= 10 && hour <= 22);

        int[] hours = {9, 20};
        for (int hour : hours) {
            System.out.println("--- Hour " + hour + " ---");
            for (Switchable device : devices) {
                boolean allowed = eveningLights.mayTurnOn(device, hour)
                        && daytimeFans.mayTurnOn(device, hour);
                System.out.print(device + " may switch on? " + allowed + " -> ");
                if (allowed) {
                    device.on();
                    device.off();
                } else {
                    System.out.println("blocked by policy");
                }
            }
        }
    }
}
