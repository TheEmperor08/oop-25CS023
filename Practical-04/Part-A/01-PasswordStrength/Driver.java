/**
 * Practical 04 - Part A - Program 1: Password strength (driver).
 * Tests several passwords and prints which rules passed and the label.
 */
public class Driver {

    public static void main(String[] args) {
        String[] passwords = {"abc", "abcdefgh", "Abcdefgh", "Abcd1234", "Abcd1234!"};

        for (String pw : passwords) {
            StringBuilder rules = new StringBuilder();
            if (PasswordChecker.hasMinLength(pw)) {
                rules.append("length ");
            }
            if (PasswordChecker.hasUppercase(pw)) {
                rules.append("uppercase ");
            }
            if (PasswordChecker.hasDigit(pw)) {
                rules.append("digit ");
            }
            if (PasswordChecker.hasSpecial(pw)) {
                rules.append("special ");
            }
            String passed = rules.length() == 0 ? "none" : rules.toString().trim();
            System.out.printf("%-10s passed: %-34s -> %s%n",
                    pw, passed, PasswordChecker.strength(pw));
        }
    }
}
