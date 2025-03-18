public class Student {
    // Private attributes (Encapsulation)
    private String name;
    private int rollNumber;
    private double marks;

    // Constructor
    public Student(String name, int rollNumber, double marks) {
        this.name = name;
        this.rollNumber = rollNumber;
        this.marks = marks;
    }

    // Getters/Setters (Encapsulation)
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getRollNumber() { return rollNumber; }
    public void setRollNumber(int rollNumber) { this.rollNumber = rollNumber; }

    public double getMarks() { return marks; }
    public void setMarks(double marks) { this.marks = marks; }

    // Method to display details (used later for polymorphism)
    public void displayDetails() {
        System.out.println("Student: " + name + ", Roll: " + rollNumber);
    }
}