import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Practical 09 - Part A - Program 2: Seat booking race.
 * A show has 5 seats and 10 threads each try to book one. The unsafe check-then-act
 * oversells; the synchronized book() lets exactly 5 bookings succeed.
 */
public class SeatBooking {

    /** A show with a shared seat counter. */
    static class Show {
        private int seatsLeft = 5;

        boolean bookUnsafe() {
            if (seatsLeft > 0) {     // check ...
                pause();             // ... the gap where other threads slip in ...
                seatsLeft--;         // ... then act
                return true;
            }
            return false;
        }

        synchronized boolean bookSafe() {
            if (seatsLeft > 0) {
                pause();
                seatsLeft--;
                return true;
            }
            return false;
        }

        int getSeatsLeft() {
            return seatsLeft;
        }

        private static void pause() {
            try {
                Thread.sleep(5);     // simulates payment processing
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    static void run(boolean synchronize) throws InterruptedException {
        Show show = new Show();
        List<String> booked = Collections.synchronizedList(new ArrayList<>());
        CountDownLatch startGate = new CountDownLatch(1);
        Thread[] customers = new Thread[10];
        for (int i = 0; i < customers.length; i++) {
            customers[i] = new Thread(() -> {
                try {
                    startGate.await();               // everyone starts together
                } catch (InterruptedException e) {
                    return;
                }
                boolean ok = synchronize ? show.bookSafe() : show.bookUnsafe();
                if (ok) {
                    booked.add(Thread.currentThread().getName());
                }
            }, "C" + (i + 1));
            customers[i].start();
        }
        startGate.countDown();
        for (Thread c : customers) {
            c.join();
        }
        List<String> names = new ArrayList<>(booked);
        names.sort((a, b) -> Integer.compare(Integer.parseInt(a.substring(1)),
                Integer.parseInt(b.substring(1))));
        System.out.println((synchronize ? "With synchronized book(): "
                : "Without synchronization: ")
                + names.size() + " bookings succeeded, seatsLeft = " + show.getSeatsLeft());
        System.out.println("  booked by " + names
                + (names.size() > 5 ? "  <-- OVERSOLD" : "  (exactly 5)"));
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Seats: 5, customers: 10");
        run(false);
        run(true);
    }
}
