/**
 * Practical 05 - Part A - Program 2: Payroll (main).
 * Loops over a mixed Employee[] and uses instanceof only to add an intern note.
 */
public class Payroll {

    public static void main(String[] args) {
        Employee[] staff = {
            new FullTime("Asha", "E101", 55000),
            new PartTime("Ravi", "E102", 80, 350),
            new Intern("Neel", "E103", 15000, "Asha"),
            new FullTime("Meera", "E104", 62000)
        };

        double total = 0;
        for (Employee e : staff) {
            double salary = e.monthlySalary();
            total += salary;
            String line = String.format("%-5s %-6s %-9s %10.2f",
                    e.getId(), e.getName(), e.getClass().getSimpleName(), salary);
            if (e instanceof Intern intern) {
                line += "   (intern - stipend only, mentor: " + intern.getMentor() + ")";
            }
            System.out.println(line);
        }
        System.out.printf("Total monthly payroll: %.2f%n", total);
    }
}
