/** Practical 05 - Part A - Program 2: a full-time employee with a fixed salary. */
public class FullTime extends Employee {

    private final double fixedSalary;

    public FullTime(String name, String id, double fixedSalary) {
        super(name, id);
        this.fixedSalary = fixedSalary;
    }

    public double monthlySalary() {
        return fixedSalary;
    }
}
