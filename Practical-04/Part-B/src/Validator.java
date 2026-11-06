import java.util.regex.Pattern;

/**
 * Input validation for MiniBank using pre-compiled regular expressions.
 * matcher(...).matches() checks the WHOLE string, which is the same as
 * anchoring the pattern with ^ and $.
 */
public class Validator {

    /** Indian mobile: 10 digits starting with 6, 7, 8 or 9. */
    private static final Pattern MOBILE = Pattern.compile("^[6-9][0-9]{9}$");
    /** Simple e-mail: local part, @, domain with at least one dot. */
    private static final Pattern EMAIL =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    /** PAN: 5 letters, 4 digits, 1 letter (e.g. ABCDE1234F). */
    private static final Pattern PAN = Pattern.compile("^[A-Z]{5}[0-9]{4}[A-Z]$");
    /** IFSC: 4 letters, the digit 0, then 6 letters/digits (e.g. SBIN0001234). */
    private static final Pattern IFSC = Pattern.compile("^[A-Z]{4}0[A-Z0-9]{6}$");
    /** Supplementary: a positive whole number without leading zeros. */
    private static final Pattern AMOUNT = Pattern.compile("^[1-9][0-9]{0,14}$");

    private Validator() {
        // utility class: no objects
    }

    public static boolean isValidMobile(String mobile) {
        return mobile != null && MOBILE.matcher(mobile).matches();
    }

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL.matcher(email).matches();
    }

    public static boolean isValidPan(String pan) {
        return pan != null && PAN.matcher(pan).matches();
    }

    public static boolean isValidIfsc(String ifsc) {
        return ifsc != null && IFSC.matcher(ifsc).matches();
    }

    /** Supplementary: accepts only positive whole numbers such as "500". */
    public static boolean isValidAmount(String amount) {
        return amount != null && AMOUNT.matcher(amount).matches();
    }
}
