import sun.jvm.hotspot.opto.Node;
import sun.jvm.hotspot.opto.Node_List;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class FineGrainedLock {

    private Lock lock = new ReentrantLock();

    private class Node{
        Object item;
        int key;
        Node next;
        public void lock(){
            lock.lock();
        }
        public void unlock(){
            lock.unlock();
        }
    }

    private Node head;

    public boolean contains(Object item){
        int key = item.hashCode();

        Node pred = null;
        Node curr = null;

        head.lock();
        try {
            pred = head;
            curr = pred.next;
            curr.lock();
            try {

                while (curr.key < key) {
                    pred.unlock();
                    pred = curr;
                    curr = curr.next;
                    curr.lock();
                }
                if (curr.key == key) {
                    return true;
                } else {
                    return false;
                }
            }finally {
                curr.unlock();
            }
        }finally{
            pred.unlock();
        }
    }
}
