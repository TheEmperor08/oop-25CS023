/**
 * Practical 07 - Part A - Program 2: Mini test runner.
 * Some methods are marked @Run; the others must be ignored by the runner.
 */
public class CalculatorChecks {

    @Run
    public void additionWorks() {
        check(2 + 3 == 5, "2 + 3 should be 5");
    }

    @Run
    public void multiplicationWorks() {
        check(4 * 5 == 20, "4 * 5 should be 20");
    }

    @Run
    public void integerDivisionTruncates() {
        check(7 / 2 == 4, "7 / 2 should be 4");   // deliberately wrong: shows a failure
    }

    public void helperNotATest() {
        System.out.println("  !! helperNotATest should never run");
    }

    public void anotherUnmarkedMethod() {
        System.out.println("  !! anotherUnmarkedMethod should never run");
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
