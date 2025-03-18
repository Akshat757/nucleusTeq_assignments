public class EvenSum {
    public static void main(String[] args) {
        int sum = 0, i = 1;
        while (i <= 10) {
            if (i % 2 == 0) {
                sum += i;
            }
            i++;
        }
        System.out.println("Sum: " + sum);
    }
}