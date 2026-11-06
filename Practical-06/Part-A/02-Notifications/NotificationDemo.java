/**
 * Practical 06 - Part A - Program 2: Notification senders.
 * Email and SMS senders are lambdas held in an array; one message is broadcast
 * through all of them. Senders marked with the Urgent marker interface send twice.
 */
public class NotificationDemo {

    public static void main(String[] args) {
        Notifier email = msg -> System.out.println("[EMAIL] to riya@example.com: " + msg);
        // An intersection cast lets a lambda also carry the Urgent marker interface.
        Notifier sms = (Notifier & Urgent) msg ->
                System.out.println("[SMS] to 9876543210: " + msg);
        Notifier push = msg -> System.out.println("[PUSH] to app user: " + msg);

        Notifier[] senders = {email, sms, push};
        broadcast(senders, "Your OTP is 482913");
    }

    /** Sends the message through every sender; Urgent senders send it twice. */
    static void broadcast(Notifier[] senders, String message) {
        int delivered = 0;
        for (Notifier sender : senders) {
            int times = (sender instanceof Urgent) ? 2 : 1;
            for (int i = 0; i < times; i++) {
                sender.send(message);
                delivered++;
            }
        }
        System.out.println("Senders: " + senders.length
                + ", messages delivered: " + delivered);
    }
}
