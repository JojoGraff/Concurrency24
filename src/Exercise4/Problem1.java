package Exercise4;

import java.util.concurrent.locks.ReentrantLock;

class Node {
    int value;
    ReentrantLock lock = new ReentrantLock();
    Node next;

    Node(int value) {
        this.value = value;
    }
}

class ConcurrentList {
    private Node head;

    public ConcurrentList(int value) {
        head = new Node(value);
    }

    // Insert operation
    public void insert(int value) {
        Node nodeToInsert = new Node(value);

        Node current = head;
        current.lock.lock();

        if (current.value >= value){
            nodeToInsert.next = head;
            head = nodeToInsert;
            current.lock.unlock();
            return;
        }

        Node next = current.next;
        if (next != null) {
            next.lock.lock(); // Lock the next node
        }

        try {
            while (next != null && next.value < value) {
                if (next.next != null) {
                    next.next.lock.lock(); // Lock the next node
                }
                current.lock.unlock();
                current = next;
                next = current.next;
            }

            nodeToInsert.next = next;
            current.next = nodeToInsert;
        } finally {
            if (next != null) {
                next.lock.unlock(); // Unlock the next node
            }
            current.lock.unlock(); // Unlock the head
        }
    }

    // Count operation
    public int count() {
        Node current = head;
        int count = 0;

        while (current != null) {
            current.lock.lock(); // Lock each node
            count++;
            Node next = current.next;
            if (next != null) {
                next.lock.unlock(); // Unlock the next node
            }
            current = next;
        }

        return count;
    }

    public void print() {
        head.lock.lock();
        try{
            Node curr = head;
            while (curr != null){
                curr.lock.lock();
                try{
                    System.out.print(curr.value + " ");
                    curr = curr.next;
                } finally {
                    if (curr != null && curr.lock.isHeldByCurrentThread()){
                        curr.lock.unlock();
                    }
                }
            }
            System.out.println();
        } finally {
            if(head.lock.isHeldByCurrentThread()){
                head.lock.unlock();
            }
        }
    }
}
