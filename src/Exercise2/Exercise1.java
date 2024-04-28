package Exercise2;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

public class Exercise1 {

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        String block = "new block";
        BigInteger target = new BigInteger("1FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF", 16);
        
        long nonce = proveWork(block, target);

        long endTime = System.currentTimeMillis();
        System.out.println("winner nonce: " + nonce);
        System.out.println("Time: " + (endTime - startTime) + " milliseconds");
    }

    public static long proveWork(String block, BigInteger target) {
        long nonce = 0;
        while (true) {
            boolean success = checkHash(block, nonce, target);
            if (success) {
                return nonce;
            }
            nonce++;
        }
    }

    public static boolean checkHash(String block, long nonce, BigInteger target) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String input = block + nonce;
            byte[] encodedhash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            byte[] encodedhash2 = digest.digest(encodedhash);
            BigInteger hashValue = new BigInteger(1, encodedhash2);
            return hashValue.compareTo(target) < 0;
        } catch (NoSuchAlgorithmException e) {
            System.err.println("NoSuchAlgorithmException");
            return false;
        }
    }
}
