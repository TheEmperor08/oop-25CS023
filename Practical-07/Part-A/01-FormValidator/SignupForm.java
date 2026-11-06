/**
 * Practical 07 - Part A - Program 1: Form validator.
 * A sign-up form whose fields carry validation annotations.
 */
public class SignupForm {

    @NotBlank
    @MaxLength(12)
    private final String username;

    @NotBlank
    @MaxLength(30)
    private final String email;

    @MaxLength(15)
    private final String city;

    public SignupForm(String username, String email, String city) {
        this.username = username;
        this.email = email;
        this.city = city;
    }

    @Override
    public String toString() {
        return "SignupForm[username=" + username + ", email=" + email + ", city=" + city + "]";
    }
}
