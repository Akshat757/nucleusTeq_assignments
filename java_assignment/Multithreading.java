class NumberThread extends Thread {
    @Override
    public void run() {
        for (int i = 1; i <= 5; i++) {
            System.out.println("Number: " + i);
        }
    }
}

class LetterThread extends Thread {
    @Override
    public void run() {
        for (char c = 'A'; c <= 'E'; c++) {
            System.out.println("Letter: " + c);
        }
    }
}

public class Multithreading {
    public static void main(String[] args) {
        NumberThread t1 = new NumberThread();
        LetterThread t2 = new LetterThread();
        t1.start(); // Starts Thread 1
        t2.start(); // Starts Thread 2
    }
}