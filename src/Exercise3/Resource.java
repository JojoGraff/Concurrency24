package Exercise3;

public class Resource {
    
    private volatile Resource rs = null;

    public Resource getExpResource(){
        // Double Checked Locking
        if (rs == null) {   //check
            synchronized(this){ // lock
                //lazy initialization
                if (rs == null) {   //check
                    rs = new Resource();
                    // 1. construct empty Resource()
                    // 2. assign to rs
                    // 3. call constructor
                }
            }
        }
        return rs;
    }

    public Resource(){
        int field1= // some CPU heavy logic
        int field2= // some value from DB
        int field3= // etc.
    }
}
