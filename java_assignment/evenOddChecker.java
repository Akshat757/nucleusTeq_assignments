
import java.util.Scanner;

public class evenOddChecker {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("enter a number: ");
        int num = scanner.nextInt();
        System.out.println(num % 2 == 0 ? "number is even" : "number is odd");

        scanner.close();
    }
}
