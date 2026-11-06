/**
 * Practical 04 - Part A - Program 1: Password strength.
 * Checks four rules with regular expressions and rates the password.
 */
public class PasswordChecker {

    public static boolean hasMinLength(String pw) {
        return pw.length() >= 8;
    }

    public static boolean hasUppercase(String pw) {
        return pw.matches(".*[A-Z].*");
    }

    public static boolean hasDigit(String pw) {
        return pw.matches(".*[0-9].*");
    }

    public static boolean hasSpecial(String pw) {
        return pw.matches(".*[^A-Za-z0-9].*");
    }

    /** Counts the rules that pass: 0-1 Weak, 2-3 Medium, 4 Strong. */
    public static String strength(String pw) {
        int passed = 0;
        if (hasMinLength(pw)) {
            passed++;
        }
        if (hasUppercase(pw)) {
            passed++;
        }
        if (hasDigit(pw)) {
            passed++;
        }
        if (hasSpecial(pw)) {
            passed++;
        }
        if (passed <= 1) {
            return "Weak";
        }
        return passed <= 3 ? "Medium" : "Strong";
    }
}
