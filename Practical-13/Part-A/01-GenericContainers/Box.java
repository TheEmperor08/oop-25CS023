/**
 * Practical 13 - Part A - Program 1: Generic containers.
 * A generic box that holds one value of type T.
 *
 * @param <T> the type of the value stored in the box
 */
public class Box<T> {
    private T value;

    public Box(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Box[" + value + "]";
    }
}
