
import java.util.Scanner;

public class temperatureConverter {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose conversion: 1-C to F, 2-F to C");
        int choice = scanner.nextInt();
        double temp = scanner.nextDouble();

        if (choice == 1) {
            System.out.println(temp * 9/5 + 32 + "°F");
        } else {
            System.out.println((temp - 32) * 5/9 + "°C");
        }
        scanner.close();
    }
}