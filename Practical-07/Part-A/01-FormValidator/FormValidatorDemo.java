import java.util.List;

/**
 * Practical 07 - Part A - Program 1: Form validator.
 * Validates a correct form and two incorrect forms with the reflection-based checker.
 */
public class FormValidatorDemo {

    public static void main(String[] args) {
        SignupForm[] forms = {
            new SignupForm("riya_s", "riya@example.com", "Anand"),
            new SignupForm("  ", "riya@example.com", "Anand"),
            new SignupForm("a_very_long_username", "", "Vallabh Vidyanagar Gujarat")
        };
        for (SignupForm form : forms) {
            System.out.println(form);
            List<String> errors = FormValidator.validate(form);
            if (errors.isEmpty()) {
                System.out.println("  Valid - no errors");
            } else {
                System.out.println("  " + errors.size() + " error(s):");
                for (String error : errors) {
                    System.out.println("   - " + error);
                }
            }
        }
    }
}
