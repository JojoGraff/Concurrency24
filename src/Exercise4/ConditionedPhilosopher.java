package Exercise4;

import java.util.concurrent.TimeUnit;

class ConditionedPhilosopher extends Thread {
    private final char id;
    private boolean eating;
    private ConditionedPhilosopher left, right;
    private final Object tableLock; // Use an object for intrinsic locking

    public ConditionedPhilosopher(char id, Object tableLock) {
        this.id = id;
        eating = false;
        this.tableLock = tableLock;
    }

    public void setLeft(ConditionedPhilosopher left) {
        this.left = left;
    }

    public void setRight(ConditionedPhilosopher right) {
        this.right = right;
    }

    @Override
    public void run() {
        try {
            while (true) {
                think();
                eat();
            }
        } catch (InterruptedException ex) {
            // nothing to clean up, terminate
        }
    }

    private void think() throws InterruptedException {
        synchronized (tableLock) {
            eating = false;
            tableLock.notifyAll(); // Notify all philosophers
        }
        System.out.println("Philosopher " + id + " thinks for a while");
        TimeUnit.SECONDS.sleep(1);
    }

    private void eat() throws InterruptedException {
        synchronized (tableLock) {
            while (left.eating || right.eating) {
                tableLock.wait(); // Wait until both neighbors are not eating
            }
            eating = true;
        }
        System.out.println("Philosopher " + id + " eats for a while");
        TimeUnit.SECONDS.sleep(1);
    }
}
