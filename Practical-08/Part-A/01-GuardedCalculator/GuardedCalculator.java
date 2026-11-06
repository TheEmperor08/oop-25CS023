import java.util.Scanner;

/**
 * Practical 08 - Part A - Program 1: Guarded calculator.
 * Reads two numbers and an operator; division by zero raises a custom
 * DivideByZeroException and bad numbers are caught separately. The loop repeats
 * until one calculation succeeds, and a finally block logs every attempt.
 */
public class GuardedCalculator {

    static double calculate(double a, double b, String op) throws DivideByZeroException {
        return switch (op) {
            case "+" -> a + b;
            case "-" -> a - b;
            case "*" -> a * b;
            case "/" -> {
                if (b == 0) {
                    throw new DivideByZeroException("cannot divide " + a + " by zero");
                }
                yield a / b;
            }
            default -> throw new IllegalArgumentException("unknown operator '" + op + "'");
        };
    }

    /** Prints the prompt and returns the next trimmed line, or null at end of input. */
    static String ask(Scanner in, String prompt) {
        System.out.print(prompt);
        if (!in.hasNextLine()) {
            System.out.println();
            return null;
        }
        return in.nextLine().trim();
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        boolean done = false;
        int attempt = 0;

        while (!done) {
            attempt++;
            String status = "failed";
            try {
                String first = ask(in, "First number : ");
                if (first == null) {
                    status = "ended (no more input)";
                    break;                       // finally still runs
                }
                double a = Double.parseDouble(first);
                String second = ask(in, "Second number: ");
                if (second == null) {
                    status = "ended (no more input)";
                    break;
                }
                double b = Double.parseDouble(second);
                String op = ask(in, "Operator (+ - * /): ");
                if (op == null) {
                    status = "ended (no more input)";
                    break;
                }
                double result = calculate(a, b, op);
                System.out.println("Result: " + a + " " + op + " " + b + " = " + result);
                status = "succeeded";
                done = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid number - please enter digits only ("
                        + e.getMessage() + ")");
            } catch (DivideByZeroException e) {
                System.out.println("Math error: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Input error: " + e.getMessage());
            } finally {
                System.out.println("[log] attempt " + attempt + " " + status);
            }
        }
        in.close();
    }
}
