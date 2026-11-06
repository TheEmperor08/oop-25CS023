/**
 * Practical 09 - Part A - Program 1: Counter race.
 * Several threads increment one shared counter many times. Without
 * synchronization updates are lost and the total comes out too low; with a
 * synchronized increment the total is exactly right on every run.
 */
public class CounterRace {

    static final int THREADS = 4;
    static final int INCREMENTS = 200_000;

    /** Shared counter with an unsafe and a safe increment. */
    static class Counter {
        private int value;

        void unsafeIncrement() {
            value++;                 // read-modify-write: not atomic
        }

        synchronized void safeIncrement() {
            value++;                 // only one thread at a time
        }

        int get() {
            return value;
        }
    }

    /** Starts THREADS threads that each call the given action INCREMENTS times. */
    static int run(boolean synchronize) throws InterruptedException {
        Counter counter = new Counter();
        Thread[] workers = new Thread[THREADS];
        for (int t = 0; t < THREADS; t++) {
            workers[t] = new Thread(() -> {
                for (int i = 0; i < INCREMENTS; i++) {
                    if (synchronize) {
                        counter.safeIncrement();
                    } else {
                        counter.unsafeIncrement();
                    }
                }
            }, "worker-" + t);
            workers[t].start();
        }
        for (Thread worker : workers) {
            worker.join();
        }
        return counter.get();
    }

    public static void main(String[] args) throws InterruptedException {
        int expected = THREADS * INCREMENTS;
        System.out.println(THREADS + " threads x " + INCREMENTS + " increments, expected "
                + expected);
        for (int trial = 1; trial <= 3; trial++) {
            int total = run(false);
            System.out.println("Without synchronization, run " + trial + ": " + total
                    + " (lost " + (expected - total) + ")");
        }
        for (int trial = 1; trial <= 3; trial++) {
            int total = run(true);
            System.out.println("With synchronized,       run " + trial + ": " + total
                    + (total == expected ? " (correct)" : " (WRONG)"));
        }
    }
}
