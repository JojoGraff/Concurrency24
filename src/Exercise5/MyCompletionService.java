package Exercise5;

import java.util.concurrent.*;
public class MyCompletionService<T> {
    private final MyCallableExecutor executor;
    private final LinkedBlockingQueue<Future<T>> queue;

    public MyCompletionService(MyCallableExecutor executor) {
        this.executor = executor;
        this.queue = new LinkedBlockingQueue<>();
    }

    public Future<T> submit(Callable<T> task) {

        FutureTask<T> futureTask = new FutureTask<T>(task);
        executor.executeCallable(futureTask);
        queue.add(futureTask);
        return futureTask;
    }

    public Future<T> take() throws InterruptedException {
        return queue.take();
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        MyCallableExecutor executor = new MyCallableExecutor(3);
        MyCompletionService<Integer> completionService = new MyCompletionService<>(executor);

        completionService.submit(() -> 4*5);
        completionService.submit(() -> 4*10);
        completionService.submit(() -> 4*15);
        for(int i = 0; i < 3; i++) {
            System.out.println(completionService.take().get());
        }
        executor.shutdown();
    }
}
