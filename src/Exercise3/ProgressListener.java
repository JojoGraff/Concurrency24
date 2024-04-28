package Exercise3;

public class ProgressListener{

    public final Object lock = new Object();

    public void onProgress(int total) {
        synchronized (lock) {
            System.out.println("Downloaded bytes: " + total);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}