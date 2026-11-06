/**
 * Practical 04 - Part A - Program 3 (For Advanced Learners): Template filler (driver).
 */
public class Driver {

    public static void main(String[] args) {
        String template = "Dear {name}, order {id} ships {date}.";
        String[] names = {"name", "id"};
        String[] values = {"Riya", "A07"};      // {date} is not supplied

        System.out.println("Template: " + template);
        System.out.println("Filled  : " + TemplateFiller.fill(template, names, values));
    }
}
