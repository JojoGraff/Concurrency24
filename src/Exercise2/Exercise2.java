package Exercise2;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class Exercise2 {

    private static final AtomicLong winnerNonce = new AtomicLong(-1);
    private static volatile long currentNonce = 0;

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        int numThreads = 3;

        // Create a thread pool with fixed size (same as available processors)
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        for (int i = 0; i < numThreads; i++) {
            executor.execute(new ConcurrentTask());
        }

        // Shutdown the thread pool when all tasks are done
        executor.shutdown();

        try {
            executor.awaitTermination(120, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.err.println("Thread pool was interrupted while waiting for tasks to complete.");
        }

        long endTime = System.currentTimeMillis();
        System.out.println("winner nonce: " + winnerNonce);
        System.out.println("Time: " + (endTime - startTime) + " milliseconds");
    }

    public static void checkHash(String block, long nonce, BigInteger target) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String input = block + nonce;
            byte[] encodedhash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            byte[] encodedhash2 = digest.digest(encodedhash);
            BigInteger hashValue = new BigInteger(1, encodedhash2);
            if (hashValue.compareTo(target) < 0) {
                winnerNonce.set(nonce);
            }
        } catch (NoSuchAlgorithmException e) {
            System.err.println("NoSuchAlgorithmException");
        }
    }

    static class ConcurrentTask implements Runnable {
        private String block = "new block";
        private BigInteger target = new BigInteger("1FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF", 16);

        public ConcurrentTask() {
        }

        @Override
        public void run() {
            while (winnerNonce.get() == -1) {
                checkHash(block, currentNonce, target);
                currentNonce++;
            }
        }
    }
}
