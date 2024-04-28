package Exercise5;

import java.util.concurrent.*;

public class MyCallableExecutor {

    private class PoolWorker extends Thread {
        public void run() {
            while(true) {
                FutureTask<?> task = null;
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

    private final LinkedBlockingQueue<FutureTask<?>> queue;
    private final PoolWorker[] threads;

    private volatile boolean shutdown = false;

    public MyCallableExecutor(int threadPoolSize) {
        queue = new LinkedBlockingQueue<>();
        threads = new PoolWorker[threadPoolSize];

        for (int i = 0; i < threadPoolSize; i++) {
            threads[i] = new PoolWorker();
            threads[i].start();
        }
    }

    public void execute(Runnable task) {
        if(!this.shutdown) {
            FutureTask<Void> futureTask = new FutureTask<>(task, null);
            synchronized (queue) {
                queue.add(futureTask);
                queue.notify();
            }
        }
        else {
            throw new RuntimeException("Executor is shutdown");
        }

    }

    public <T> Future<T> executeCallable(Callable<T> task) {
        if(!this.shutdown) {
            FutureTask<T> futureTask = new FutureTask<>(task);
            synchronized (queue) {
                queue.add(futureTask);
                queue.notify();
            }
            return futureTask;
        }
        else {
            throw new RuntimeException("Executor is shutdown");
        }
    }

    public <T> Future<T> executeCallable(FutureTask<T> task) {
        if(!this.shutdown) {
            synchronized (queue) {
                queue.add(task);
                queue.notify();
            }
            return task;
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


    public static void main(String[] args) throws InterruptedException, ExecutionException {
        MyCallableExecutor executor = new MyCallableExecutor(5);
        for(int i=1; i <= 3; i++) {
            int finalI = i;
            executor.execute(() -> System.out.println("This is thread " + finalI));
        }
        Future<Integer> future = executor.executeCallable(() -> {
            int sum = 0;
            for (int i = 0; i < 10; i++) {
                sum += i;
            }
            return sum;

        });
        System.out.println("Result of Future: " + future.get());

        executor.shutdown();

    }

}
