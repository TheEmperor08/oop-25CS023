/** Practical 05 - Part A - Program 2: an intern paid a fixed stipend. */
public class Intern extends Employee {

    private final double stipend;
    private final String mentor;

    public Intern(String name, String id, double stipend, String mentor) {
        super(name, id);
        this.stipend = stipend;
        this.mentor = mentor;
    }

    public String getMentor() {
        return mentor;
    }

    public double monthlySalary() {
        return stipend;
    }
}
