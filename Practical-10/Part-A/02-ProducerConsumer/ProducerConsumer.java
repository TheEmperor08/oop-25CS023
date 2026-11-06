/**
 * Practical 10 - Part A - Program 2: Producer-consumer.
 * One thread puts numbers into a small shared buffer and another removes them.
 * The two threads are coordinated with wait() and notify().
 */
public class ProducerConsumer {

    /** A fixed-size circular buffer shared by the producer and the consumer. */
    static class Buffer {
        private final int[] items;
        private int count;
        private int putIndex;
        private int takeIndex;

        Buffer(int capacity) {
            items = new int[capacity];
        }

        /** Adds a value, waiting while the buffer is full. */
        synchronized void put(int value) throws InterruptedException {
            while (count == items.length) {
                System.out.println("  Buffer full, producer waits");
                wait();
            }
            items[putIndex] = value;
            putIndex = (putIndex + 1) % items.length;
            count++;
            System.out.println("Produced " + value + "  (buffer size " + count + ")");
            notify();                                   // wake the waiting consumer
        }

        /** Removes the oldest value, waiting while the buffer is empty. */
        synchronized int take() throws InterruptedException {
            while (count == 0) {
                wait();
            }
            int value = items[takeIndex];
            takeIndex = (takeIndex + 1) % items.length;
            count--;
            System.out.println("Consumed " + value + "  (buffer size " + count + ")");
            notify();                                   // wake the waiting producer
            return value;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        final int total = 10;
        Buffer buffer = new Buffer(3);
        int[] received = new int[total];

        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= total; i++) {
                    buffer.put(i);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Producer");

        Thread consumer = new Thread(() -> {
            try {
                for (int i = 0; i < total; i++) {
                    received[i] = buffer.take();
                    Thread.sleep(30);                   // consumer is slower
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Consumer");

        producer.start();
        consumer.start();
        producer.join();
        consumer.join();

        boolean inOrder = true;
        for (int i = 0; i < total; i++) {
            if (received[i] != i + 1) {
                inOrder = false;
            }
        }
        System.out.println("Items consumed: " + total
                + ", in order and none lost: " + inOrder);
    }
}
