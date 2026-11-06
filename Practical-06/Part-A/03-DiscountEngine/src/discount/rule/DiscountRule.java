package discount.rule;

/**
 * Practical 06 - Part A - Program 3 (For Advanced Learners): Discount engine.
 * A discount rule maps an original price to a discounted price.
 */
@FunctionalInterface
public interface DiscountRule {

    double apply(double price);

    /** Returns a rule that applies this rule first and then the next one. */
    default DiscountRule andThen(DiscountRule next) {
        return price -> next.apply(apply(price));
    }
}
