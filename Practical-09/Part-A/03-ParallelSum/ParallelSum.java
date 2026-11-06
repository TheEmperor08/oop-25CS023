import java.util.concurrent.atomic.AtomicLong;

/**
 * Practical 09 - Part A - Program 3 (For Advanced Learners): Parallel sum.
 * A large array is split across several threads that add into one shared total.
 * Version 0 is unsafe (wrong result); fix 1 uses a synchronized block and fix 2
 * uses an AtomicLong.
 *
 * Measurement: wall-clock time with System.nanoTime(), averaged over 3 timed runs
 * after one warm-up run (so JIT compilation does not distort the first result).
 */
public class ParallelSum {

    static final int SIZE = 4_000_000;
    static final int THREADS = 4;

    interface Adder {
        void add(int value);

        long total();
    }

    /** Unsafe: plain long field, += is a non-atomic read-modify-write. */
    static class UnsafeAdder implements Adder {
        private long total;

        public void add(int value) {
            total += value;
        }

        public long total() {
            return total;
        }
    }

    /** Fix 1: every update runs inside a synchronized block on a private lock. */
    static class SynchronizedAdder implements Adder {
        private final Object lock = new Object();
        private long total;

        public void add(int value) {
            synchronized (lock) {
                total += value;
            }
        }

        public long total() {
            synchronized (lock) {
                return total;
            }
        }
    }

    /** Fix 2: lock-free atomic update. */
    static class AtomicAdder implements Adder {
        private final AtomicLong total = new AtomicLong();

        public void add(int value) {
            total.addAndGet(value);
        }

        public long total() {
            return total.get();
        }
    }

    interface AdderFactory {
        Adder create();
    }

    static long sumWithThreads(int[] data, Adder adder) throws InterruptedException {
        Thread[] workers = new Thread[THREADS];
        int chunk = data.length / THREADS;
        for (int t = 0; t < THREADS; t++) {
            int from = t * chunk;
            int to = (t == THREADS - 1) ? data.length : from + chunk;
            workers[t] = new Thread(() -> {
                for (int i = from; i < to; i++) {
                    adder.add(data[i]);
                }
            });
            workers[t].start();
        }
        for (Thread w : workers) {
            w.join();
        }
        return adder.total();
    }

    static void measure(String label, int[] data, long expected, AdderFactory factory)
            throws InterruptedException {
        sumWithThreads(data, factory.create());               // warm-up
        long totalNanos = 0;
        long result = 0;
        for (int run = 0; run < 3; run++) {
            long start = System.nanoTime();
            result = sumWithThreads(data, factory.create());
            totalNanos += System.nanoTime() - start;
        }
        System.out.printf("%-22s sum = %,15d  %-7s avg %6.1f ms%n", label, result,
                result == expected ? "correct" : "WRONG", totalNanos / 3 / 1_000_000.0);
    }

    public static void main(String[] args) throws InterruptedException {
        int[] data = new int[SIZE];
        long expected = 0;
        for (int i = 0; i < SIZE; i++) {
            data[i] = i % 1000 + 1;
            expected += data[i];
        }
        System.out.printf("%,d numbers, %d threads, expected sum = %,d%n",
                SIZE, THREADS, expected);
        measure("Unsafe shared total", data, expected, UnsafeAdder::new);
        measure("Fix 1: synchronized", data, expected, SynchronizedAdder::new);
        measure("Fix 2: AtomicLong", data, expected, AtomicAdder::new);
    }
}
