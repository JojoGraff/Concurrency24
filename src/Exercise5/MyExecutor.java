package Exercise5;

import java.util.concurrent.LinkedBlockingQueue;

public class MyExecutor {

    private class PoolWorker extends Thread {
        public void run() {
            Runnable task;

            while(true) {
                synchronized (queue) {
                    while(queue.isEmpty()) {
                        try {
                            queue.wait();
                        }
                        catch (InterruptedException e) {
                            return;
                        }
                    }

                    task = queue.poll();
                }
                try {
                    task.run();
                }
                catch (RuntimeException e) {
                    System.out.println(e.getMessage());
                }

            }

        }
    }

    private final int threadPoolSize;
    private final LinkedBlockingQueue<Runnable> queue;
    private final PoolWorker[] threads;

    private volatile boolean shutdown = false;

    public MyExecutor(int threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
        queue = new LinkedBlockingQueue<>();
        threads = new PoolWorker[threadPoolSize];

        for (int i = 0; i < threadPoolSize; i++) {
            threads[i] = new PoolWorker();
            threads[i].start();
        }
    }

    public void execute(Runnable task) {
        if(!this.shutdown) {
            synchronized (queue) {
                queue.add(task);
                queue.notify();
            }
        }
        else {
            throw new RuntimeException("Executor is shutdown");
        }

    }


    public void shutdown() {
        this.shutdown = true;
        for (PoolWorker thread: threads) {
            thread.interrupt();
        }

    }


    public static void main(String[] args) {
        MyExecutor executor = new MyExecutor(5);
        for(int i=1; i <= 3; i++) {
            int finalI = i;
            executor.execute(() -> System.out.println("This is thread " + finalI));
        }
        executor.shutdown();

    }

}
