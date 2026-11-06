package model;

/**
 * A bank customer. All fields are private; the id is generated once and never changes.
 * Practical 03: static nested Address class and clone().
 */
public class Customer implements Cloneable {

    /** Shared by all customers: the last number used for an id. */
    private static long customerCounter = 100;

    private final String customerId;
    private String name;
    private String email;
    private String mobile;
    private Address address;

    /**
     * A postal address. Static nested: it does not need a Customer object to
     * exist and cannot see Customer's instance fields.
     */
    public static class Address {
        private String line;
        private String city;
        private String pincode;

        public Address(String line, String city, String pincode) {
            this.line = line;
            this.city = city;
            this.pincode = pincode;
        }

        public String getLine() {
            return line;
        }

        public String getCity() {
            return city;
        }

        public String getPincode() {
            return pincode;
        }

        public String toString() {
            return line + ", " + city + " - " + pincode;
        }
    }

    public Customer(String name, String email, String mobile) {
        this(name, email, mobile, null);
    }

    public Customer(String name, String email, String mobile, Address address) {
        this.customerId = generateCustomerId();
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.address = address;
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

    public Address getAddress() {
        return address;
    }

    /**
     * Returns a copy of this customer. Object.clone() copies every field;
     * Address has no setters, so sharing the same Address object is safe.
     */
    public Customer clone() {
        try {
            return (Customer) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Customer implements Cloneable", e);
        }
    }

    public String toString() {
        return customerId + " | " + name + " | " + email + " | " + mobile
                + (address != null ? " | " + address : "");
    }
}
