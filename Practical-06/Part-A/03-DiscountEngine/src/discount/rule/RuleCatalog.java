package discount.rule;

/**
 * Practical 06 - Part A - Program 3 (For Advanced Learners): Discount engine.
 * The catalogue of rules the user can pick at runtime; every rule is a lambda.
 */
public final class RuleCatalog {

    public static final String[] NAMES = {
        "10% off everything",
        "Flat 100 off items priced 1000 or more",
        "Clearance: 25% off items under 300",
        "Round down to the nearest 10"
    };

    public static final DiscountRule[] RULES = {
        price -> price * 0.90,
        price -> price >= 1000 ? price - 100 : price,
        price -> price < 300 ? price * 0.75 : price,
        price -> Math.floor(price / 10) * 10
    };

    private RuleCatalog() {
    }

    /** Returns the rule for a 1-based menu number, or null when out of range. */
    public static DiscountRule pick(int number) {
        return (number >= 1 && number <= RULES.length) ? RULES[number - 1] : null;
    }
}
