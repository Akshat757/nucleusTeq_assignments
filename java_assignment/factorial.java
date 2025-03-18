
import java.util.Scanner;

public class factorial {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("enter a number (num >= 0): ");
    
        int n = scanner.nextInt();
        int fact = 1;
        if(n < 0){
            System.out.println("invalid input");
            fact = 0;
        }

        for(int i = 1; i <= n; i++){
            fact *= i;
        }

        System.out.println("the factorial of " + n + " is: " + fact);
        scanner.close();
    }
}
