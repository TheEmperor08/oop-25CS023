import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Practical 10 - Part A - Program 1: Thread-pool runner.
 * Submits 10 Runnable tasks to a fixed pool of 3 threads and shows that the
 * same few threads are reused for all of the tasks.
 */
public class ThreadPoolRunner {

    /** Counts how many tasks each pool thread executed. */
    private static final Map<String, Integer> TASKS_PER_THREAD = new ConcurrentHashMap<>();

    /** A small task that prints its id and the thread running it, then sleeps briefly. */
    static class PrintTask implements Runnable {
        private final int id;

        PrintTask(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            String threadName = Thread.currentThread().getName();
            System.out.println("Task " + id + " running on " + threadName);
            TASKS_PER_THREAD.merge(threadName, 1, Integer::sum);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        final int taskCount = 10;
        ExecutorService pool = Executors.newFixedThreadPool(3);

        for (int i = 1; i <= taskCount; i++) {
            pool.submit(new PrintTask(i));
        }

        pool.shutdown();                                   // no new tasks accepted
        boolean finished = pool.awaitTermination(10, TimeUnit.SECONDS);

        System.out.println("All tasks finished: " + finished);
        Map<String, Integer> sorted = new TreeMap<>(TASKS_PER_THREAD);
        sorted.forEach((name, count) ->
                System.out.println(name + " executed " + count + " tasks"));
        System.out.println(taskCount + " tasks were run by only " + sorted.size()
                + " reused threads");
    }
}
