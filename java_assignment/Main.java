public class Main {
    public static void main(String[] args) {
        // Parent class object
        Student student = new Student("Alice", 101, 85.5);
        student.displayDetails(); // Uses parent method

        // Child class object
        GraduateStudent gradStudent = new GraduateStudent("Bob", 201, 90.0, "AI Ethics");
        gradStudent.displayDetails(true); // Uses overloaded method
        gradStudent.submitThesis(); // Uses child-specific method
    }
}