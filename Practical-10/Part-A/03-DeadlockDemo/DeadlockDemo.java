import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

/**
 * Practical 10 - Part A - Program 3 (For Advanced Learners): Deadlock and its fix.
 * Scenario: two office jobs need both the Scanner and the Printer.
 * A copy job locks Scanner then Printer; a fax job locks Printer then Scanner.
 * With opposite lock order they deadlock; with one consistent order they finish.
 */
public class DeadlockDemo {

    /** A shared device; the id gives every device a global lock order. */
    static class Device {
        final int id;
        final String name;

        Device(int id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    /** Locks first, pauses, then locks second (the pause makes the deadlock certain). */
    static void useBoth(String job, Device first, Device second) {
        synchronized (first) {
            System.out.println(job + " locked " + first.name);
            pause(200);
            System.out.println(job + " waiting for " + second.name);
            synchronized (second) {
                System.out.println(job + " locked " + second.name + " -> job done");
            }
        }
    }

    /** Fixed version: always lock the device with the smaller id first. */
    static void useBothOrdered(String job, Device a, Device b) {
        Device first = a.id < b.id ? a : b;
        Device second = a.id < b.id ? b : a;
        useBoth(job, first, second);
    }

    static void pause(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /** Starts the two jobs as daemon threads and reports whether they finished. */
    static void run(String title, Runnable copyJob, Runnable faxJob)
            throws InterruptedException {
        System.out.println("--- " + title + " ---");
        Thread t1 = new Thread(copyJob, "CopyJob");
        Thread t2 = new Thread(faxJob, "FaxJob");
        t1.setDaemon(true);                   // stuck daemon threads cannot keep the JVM alive
        t2.setDaemon(true);
        t1.start();
        t2.start();
        t1.join(2000);
        t2.join(2000);

        // Look only at the two threads of this run
        // (stuck threads of an earlier run are ignored).
        ThreadMXBean mx = ManagementFactory.getThreadMXBean();
        long[] ids = mx.findDeadlockedThreads();
        boolean deadlocked = false;
        if (ids != null) {
            for (ThreadInfo info : mx.getThreadInfo(ids)) {
                if (info.getThreadId() == t1.getId() || info.getThreadId() == t2.getId()) {
                    if (!deadlocked) {
                        System.out.println("DEADLOCK detected between:");
                        deadlocked = true;
                    }
                    System.out.println("  " + info.getThreadName()
                            + " holds a lock and waits for "
                            + info.getLockOwnerName() + "'s lock");
                }
            }
        }
        if (!deadlocked) {
            System.out.println("Both jobs completed, no deadlock.");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Device scanner = new Device(1, "Scanner");
        Device printer = new Device(2, "Printer");

        run("Opposite lock order",
                () -> useBoth("CopyJob", scanner, printer),
                () -> useBoth("FaxJob", printer, scanner));

        Device scanner2 = new Device(1, "Scanner");
        Device printer2 = new Device(2, "Printer");
        run("Consistent lock order (smaller id first)",
                () -> useBothOrdered("CopyJob", scanner2, printer2),
                () -> useBothOrdered("FaxJob", printer2, scanner2));
    }
}
