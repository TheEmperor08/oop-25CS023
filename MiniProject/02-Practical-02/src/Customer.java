/**
 * A bank customer. All fields are private; the id is generated once and never changes.
 */
public class Customer {

    /** Shared by all customers: the last number used for an id. */
    private static long customerCounter = 100;

    private final String customerId;
    private String name;
    private String email;
    private String mobile;

    public Customer(String name, String email, String mobile) {
        this.customerId = generateCustomerId();
        this.name = name;
        this.email = email;
        this.mobile = mobile;
    }

    /** Creates ids such as CUST101, CUST102, ... */
    private static String generateCustomerId() {
        customerCounter++;
        return "CUST" + customerCounter;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }
}
