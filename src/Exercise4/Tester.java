package Exercise4;

public class Tester {
    public static void main(String[] args) {

        //testing Problem1
        ConcurrentList list = new ConcurrentList(0);

        Thread t1 = new Thread(() -> {
            for(int i = 1; i < 50; i++){
                list.insert(i);
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i = 50; i < 100; i++){
                list.insert(i);
            }
        });
        t1.start();
        t2.start();

        try{
            t1.join();
            t2.join();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        System.out.println();
        list.print();

        // Counting the number of entries
        int count = list.count();
        System.out.println("The number of entries in the list is: " + count);        
    }
}
