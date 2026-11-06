import java.util.ArrayList;
import java.util.List;

/**
 * Practical 07 - Part A - Program 3 (For Advanced Learners): Column mapper.
 * Maps three header/data pairs: a complete one, one in a different column order
 * with a missing column, and one with an extra column and a bad number.
 */
public class ColumnMapperDemo {

    public static void main(String[] args) throws ReflectiveOperationException {
        String[][] samples = {
            {"roll,name,cgpa,semester", "25CS023,Shreyas,8.7,3"},
            {"name,roll,cgpa", "Riya,25CS041,9.1"},
            {"roll,name,cgpa,semester,city", "25CS050,Aman,eight,3,Anand"}
        };
        for (String[] sample : samples) {
            System.out.println("Header: " + sample[0]);
            System.out.println("Row   : " + sample[1]);
            List<String> warnings = new ArrayList<>();
            Student student = ColumnMapper.map(Student.class, sample[0], sample[1], warnings);
            System.out.println("Result: " + student);
            for (String warning : warnings) {
                System.out.println("  warning: " + warning);
            }
            System.out.println();
        }
    }
}
