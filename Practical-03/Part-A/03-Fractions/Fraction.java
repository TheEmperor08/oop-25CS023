import java.util.Objects;

/**
 * Practical 03 - Part A - Program 3 (For Advanced Learners): Fractions.
 * A fraction stored in lowest terms, so equal values compare equal.
 */
public class Fraction {

    private int num;
    private int den;

    public Fraction(int num, int den) {
        if (den == 0) {
            throw new IllegalArgumentException("Denominator cannot be zero");
        }
        if (den < 0) {              // keep the sign on the numerator
            num = -num;
            den = -den;
        }
        int g = gcd(Math.abs(num), den);
        this.num = num / g;
        this.den = den / g;
    }

    /** Greatest common divisor (Euclid); gcd(0, d) = d. */
    private static int gcd(int a, int b) {
        while (b != 0) {
            int t = a % b;
            a = b;
            b = t;
        }
        return a == 0 ? 1 : a;
    }

    public String toString() {
        return num + "/" + den;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Fraction)) {
            return false;
        }
        Fraction other = (Fraction) o;
        return num == other.num && den == other.den;
    }

    public int hashCode() {
        return Objects.hash(num, den);
    }
}
