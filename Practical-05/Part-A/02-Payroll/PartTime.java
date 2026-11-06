/** Practical 05 - Part A - Program 2: a part-time employee paid hours x rate. */
public class PartTime extends Employee {

    private final int hours;
    private final double rate;

    public PartTime(String name, String id, int hours, double rate) {
        super(name, id);
        this.hours = hours;
        this.rate = rate;
    }

    public double monthlySalary() {
        return hours * rate;
    }
}
