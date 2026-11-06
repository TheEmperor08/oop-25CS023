/**
 * Practical 07 - Part A - Program 3 (For Advanced Learners): Column mapper.
 * Target object: each field names the column it is filled from.
 */
public class Student {

    @Column(name = "roll")
    private String roll;

    @Column(name = "name")
    private String name;

    @Column(name = "cgpa")
    private double cgpa;

    @Column(name = "semester")
    private int semester;

    @Override
    public String toString() {
        return "Student[roll=" + roll + ", name=" + name + ", cgpa=" + cgpa
                + ", semester=" + semester + "]";
    }
}
