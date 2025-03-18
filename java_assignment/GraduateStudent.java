public class GraduateStudent extends Student {
    // Additional feature
    private String thesisTopic;

    // Constructor
    public GraduateStudent(String name, int rollNumber, double marks, String thesisTopic) {
        super(name, rollNumber, marks); // Call parent constructor
        this.thesisTopic = thesisTopic;
    }

    // Additional method
    public void submitThesis() {
        System.out.println("Thesis submitted: " + thesisTopic);
    }

    // Overloaded method (Polymorphism: Same name, different parameters)
    public void displayDetails(boolean includeThesis) {
        super.displayDetails(); // Call parent method
        if (includeThesis) {
            System.out.println("Thesis Topic: " + thesisTopic);
        }
    }
}