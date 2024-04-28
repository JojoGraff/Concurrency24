package Exercise3;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Problem5 {

    final static Lock lock = new ReentrantLock();
    public static volatile int lastNumber = 0;
    public static volatile List<Integer> lastFactors = new ArrayList<>();

    public Problem5() {

    }
 
    public static List<Integer> service(int input){

        int snapshotLastNumber;
        List<Integer> snapshotLastFactors;
        
        lock.lock();
        try {
            snapshotLastNumber = lastNumber;
            snapshotLastFactors = lastFactors;
        } finally {
            lock.unlock();
        }

        if (input == snapshotLastNumber) {
            return snapshotLastFactors;
        }

        List<Integer> factors = factor(input);

        lock.lock();
        try {
             lastNumber = input;
             lastFactors = factors;
         } finally {
             lock.unlock();
         }
        
        return factors;
    }

    public static List<Integer> factor(int number) {
        List<Integer> factors = new ArrayList<>();
        for (int i = 1; i <= number; i++) {
            if (number % i == 0) {
                factors.add(i);
            }
        }
        return factors;
    }

    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(() -> {
                service(10);
        });
        Thread thread2 = new Thread(() -> {
                service(20); 
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
        System.out.println(lastNumber);
        System.out.println(lastFactors);
    }
}


