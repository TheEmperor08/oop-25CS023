/**
 * Practical 13 - Part A - Program 1: Generic containers.
 * A generic pair of two values that may have different types.
 *
 * @param <A> type of the first value
 * @param <B> type of the second value
 */
public class Pair<A, B> {
    private final A first;
    private final B second;

    public Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    public A getFirst() {
        return first;
    }

    public B getSecond() {
        return second;
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ")";
    }
}
