
import java.util.Scanner;

public class fibonacci {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("enter a limit: ");
        int limit = scanner.nextInt();

        int a = 0, b = 1;
        System.out.print("Fibonacci Sequence: " + a);
        System.out.print( " " + b);
        
        while(b + a <= limit){
            int temp = b;
            b += a;
            System.out.print( " " + b);
            a = temp;
        }

        scanner.close();
    }
}
