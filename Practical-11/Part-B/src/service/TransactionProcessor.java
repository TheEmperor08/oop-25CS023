package service;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Runs transaction tasks on a fixed pool of 4 reusable threads instead of
 * creating a new thread for every task.
 */
public class TransactionProcessor {

    private final ExecutorService pool = Executors.newFixedThreadPool(4);

    /** Supplementary: how many tasks each pool thread executed. */
    private final Map<String, AtomicInteger> tasksPerThread = new ConcurrentHashMap<>();

    /** Hands a task to the pool; one of the 4 threads will run it. */
    public void submit(Runnable task) {
        pool.execute(() -> {
            tasksPerThread.computeIfAbsent(Thread.currentThread().getName(),
                    name -> new AtomicInteger()).incrementAndGet();
            task.run();
        });
    }

    /** Stops accepting tasks and waits for the submitted ones to finish. */
    public void stop() throws InterruptedException {
        pool.shutdown();
        if (!pool.awaitTermination(30, TimeUnit.SECONDS)) {
            pool.shutdownNow();
        }
    }

    /** Thread name -> number of tasks it ran (sorted by name). */
    public Map<String, Integer> getTasksPerThread() {
        Map<String, Integer> result = new TreeMap<>();
        tasksPerThread.forEach((name, count) -> result.put(name, count.get()));
        return result;
    }
}
