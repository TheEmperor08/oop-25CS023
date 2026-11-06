/**
 * Practical 03 - Part A - Program 3 (For Advanced Learners): Fractions (driver).
 * Shows that equivalent fractions print the same and are equal.
 */
public class Driver {

    public static void main(String[] args) {
        Fraction half = new Fraction(1, 2);
        Fraction twoFourths = new Fraction(2, 4);
        Fraction threeSixths = new Fraction(3, 6);
        Fraction threeQuarters = new Fraction(6, 8);
        Fraction negative = new Fraction(2, -4);

        System.out.println("new Fraction(1, 2)  prints " + half);
        System.out.println("new Fraction(2, 4)  prints " + twoFourths);
        System.out.println("new Fraction(3, 6)  prints " + threeSixths);
        System.out.println("new Fraction(6, 8)  prints " + threeQuarters);
        System.out.println("new Fraction(2, -4) prints " + negative);

        System.out.println("2/4 equals 1/2: " + twoFourths.equals(half));
        System.out.println("3/6 equals 1/2: " + threeSixths.equals(half));
        System.out.println("3/6 equals 2/4: " + threeSixths.equals(twoFourths));
        System.out.println("6/8 equals 1/2: " + threeQuarters.equals(half));
        System.out.println("Same hashCode for 1/2 and 2/4: "
                + (half.hashCode() == twoFourths.hashCode()));

        try {
            new Fraction(1, 0);
        } catch (IllegalArgumentException e) {
            System.out.println("new Fraction(1, 0) rejected: " + e.getMessage());
        }
    }
}
