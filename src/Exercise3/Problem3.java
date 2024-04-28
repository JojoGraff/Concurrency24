package Exercise3;

public class Problem3 {

    private static final int NUM_ITERATIONS = 1000000;

    public synchronized void syncMethod() {
        int i = 10;
        int j = 20;
        int sum = i + j;
    }

    public void unsyncMethod() {
        int i = 10;
        int j = 20;
        int sum = i + j;
    }

    public static void main(String[] args) {
        Problem3 lockTest = new Problem3();

        long startTimeSync = System.currentTimeMillis();
        for (int i = 0; i < NUM_ITERATIONS; i++) {
            lockTest.syncMethod();
        }
        long endTimeSync = System.currentTimeMillis();

        long startTimeUnSync = System.currentTimeMillis();
        for (int i = 0; i < NUM_ITERATIONS; i++) {
            lockTest.unsyncMethod();
        }
        long endTimeUnSync = System.currentTimeMillis();

        long syncTime = endTimeSync - startTimeSync;
        long unSyncTime = endTimeUnSync - startTimeUnSync;
        float diff = unSyncTime / syncTime;

        System.out.println("Time for synchronized method: " + syncTime + " ms");
        System.out.println("Time for unsynchronized method: " + unSyncTime + " ms");
        System.out.println("Differenz :" + diff);
    }
}