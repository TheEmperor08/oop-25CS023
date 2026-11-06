import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Practical 11 - Part A - Program 1: Save/load with object serialization.
 * Saves an array of Student objects with ObjectOutputStream, reads it back into
 * fresh objects and shows that the transient field is not saved.
 */
public class SaveLoad {

    /** A small serializable class; the password is transient and therefore not saved. */
    static class Student implements Serializable {
        private static final long serialVersionUID = 1L;

        private final String name;
        private final int rollNo;
        private final double cgpa;
        private final transient String password;

        Student(String name, int rollNo, double cgpa, String password) {
            this.name = name;
            this.rollNo = rollNo;
            this.cgpa = cgpa;
            this.password = password;
        }

        boolean sameSavedValues(Student other) {
            return name.equals(other.name) && rollNo == other.rollNo && cgpa == other.cgpa;
        }

        @Override
        public String toString() {
            return rollNo + " " + name + " cgpa=" + cgpa + " password=" + password;
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Student[] students = {
            new Student("Riya", 101, 8.7, "riya@123"),
            new Student("Aman", 102, 7.9, "aman#456"),
            new Student("Neha", 103, 9.2, "neha!789")
        };
        Path file = Paths.get("students.ser");      // created in the working directory

        try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(file))) {
            out.writeObject(students);
        }
        System.out.println("Saved " + students.length + " students to " + file
                + " (" + Files.size(file) + " bytes)");
        for (Student s : students) {
            System.out.println("  before: " + s);
        }

        Student[] loaded;
        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(file))) {
            loaded = (Student[]) in.readObject();
        }
        System.out.println("Loaded " + loaded.length + " students into fresh objects");
        boolean allSurvived = true;
        for (int i = 0; i < loaded.length; i++) {
            System.out.println("  after : " + loaded[i]);
            allSurvived &= loaded[i] != students[i] && loaded[i].sameSavedValues(students[i]);
        }
        System.out.println("Name, roll no and cgpa survived: " + allSurvived);
        System.out.println("Transient password after loading: " + loaded[0].password
                + " (not saved)");
    }
}
