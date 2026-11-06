import java.util.Arrays;

/**
 * Practical 13 - Part A - Program 3 (For Advanced Learners): Generic stack.
 * A last-in-first-out stack backed by an array that grows when it is full.
 * Empty case: pop() and peek() throw IllegalStateException, and isEmpty()
 * lets the caller check first.
 *
 * @param <T> the element type
 */
public class GenericStack<T> {
    private Object[] items = new Object[2];
    private int size;

    /** Pushes an item, doubling the array when it is full. */
    public void push(T item) {
        if (size == items.length) {
            items = Arrays.copyOf(items, size * 2);
        }
        items[size++] = item;
    }

    /** Removes and returns the top item. */
    public T pop() {
        T top = peek();
        items[--size] = null;          // let the removed object be garbage collected
        return top;
    }

    /** Returns the top item without removing it. */
    @SuppressWarnings("unchecked")
    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("stack is empty");
        }
        return (T) items[size - 1];    // safe: only T values are ever pushed
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }
}
