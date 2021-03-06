import sun.jvm.hotspot.opto.Node;
import sun.jvm.hotspot.opto.Node_List;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class FineGrainedLock<T> {

    private Lock lock = new ReentrantLock();

    private class Node{
        T item;
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

    //Initialize head and tail of list

    public FineGrainedLock(){
        head = new Node();
        head.key = Integer.MIN_VALUE;
        head.next = new Node();
        head.next.key = Integer.MAX_VALUE;
    }

    //Contains method

    public boolean contains(T item){
        int key = item.hashCode();

        Node predNode = null;
        Node currNode = null;

        head.lock();
        try {
            predNode = head;
            currNode = predNode.next;
            currNode.lock();
            try {

                //Iterate to the next smaller key to our key

                while (currNode.key < key) {
                    predNode.unlock();
                    predNode = currNode;
                    currNode = currNode.next;
                    currNode.lock();
                }

                //Check if the next key is our key

                if (currNode.key == key) {
                    return true;
                } else {
                    return false;
                }
            }finally {
                currNode.unlock();
            }
        }finally{
            predNode.unlock();
        }
    }

    //Add method needed to test contain, copied from textbook

    public boolean add (T item){
        int key = item.hashCode();
        head.lock();
        Node pred = head;
        try{
            Node curr = pred.next;
            curr.lock();
            try{
                while(curr.key < key){
                    pred.unlock();
                    pred = curr;
                    curr = curr.next;
                    curr.lock();
                }
                if(curr.key == key){
                    return false;
                }
                Node newNode = new Node();
                newNode.item = item;
                newNode.key = key;
                newNode.next = curr;
                pred.next = newNode;
                return true;
            }
            finally{
                curr.unlock();
            }

        }finally{
            pred.unlock();
        }
    }
}
