package service;

import model.Command;

/**
 * A small shared array buffer between a producer thread (adds transactions) and a
 * consumer thread (processes them), coordinated with wait() and notifyAll().
 */
public class TransactionBuffer {

    private final Command[] items;
    private int head;      // next slot to take from
    private int tail;      // next slot to put into
    private int count;

    public TransactionBuffer(int capacity) {
        items = new Command[capacity];
    }

    /** Producer side: waits while the buffer is full. */
    public synchronized void put(Command command) throws InterruptedException {
        while (count == items.length) {
            wait();                       // releases the lock until a consumer takes one
        }
        items[tail] = command;
        tail = (tail + 1) % items.length;
        count++;
        notifyAll();                      // wake a waiting consumer
    }

    /** Consumer side: waits while the buffer is empty. */
    public synchronized Command take() throws InterruptedException {
        while (count == 0) {
            wait();                       // releases the lock until a producer adds one
        }
        Command command = items[head];
        items[head] = null;
        head = (head + 1) % items.length;
        count--;
        notifyAll();                      // wake a waiting producer
        return command;
    }
}
