/**
 * Practical 05 - Part A - Program 2: Payroll.
 * Abstract base class holding the shared name and id.
 */
public abstract class Employee {

    private final String name;
    private final String id;

    protected Employee(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public abstract double monthlySalary();
}
