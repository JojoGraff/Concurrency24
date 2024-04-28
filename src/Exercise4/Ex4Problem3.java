package Exercise4;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Ex4Problem3 {

    private static final ReadWriteLock rwLock = new ReentrantReadWriteLock();
    public static volatile int lastNumber = 0;
    public static volatile List<Integer> lastFactors = new ArrayList<>();

    public Ex4Problem3() {

    }
 
    public static List<Integer> service(int input){

        rwLock.readLock().lock();
        try {
             if (input == lastNumber) {
                return lastFactors;
             }
         } finally {
            rwLock.readLock().unlock();
         }

        List<Integer> factors = factor(input);

        rwLock.writeLock().lock();
        try {
             lastNumber = input;
             lastFactors = factors;
         } finally {
            rwLock.writeLock().unlock();
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


