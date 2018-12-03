import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BoundedQueueLock<T> {

    private T[] items;

    private int head=0;
    private int tail=0;

    private Lock headLock = new ReentrantLock();
    private Lock tailLock = new ReentrantLock();

    private Condition nonEmpty = headLock.newCondition();
    private Condition nonFull = tailLock.newCondition();

    public BoundedQueueLock(int cap){
        this.items = (T[]) new Object[cap];
    }

    public void enqueue(T item){
        tailLock.lock();
        try{

            // Wait the queue to be non-full

            while (tail-head == items.length) {
                try {
                    nonFull.await();
                } catch (InterruptedException e) {

                }
            }
            items[tail % items.length] = item;
            tail++;

            if(tail-head == 1){
                nonEmpty.signal();
            }

        } finally {
            tailLock.unlock();
        }
    }

    public T dequeue(){
        headLock.lock();
        try{

            //wait for the queue to be non-empty

            while(tail - head ==0) {
                try {
                    nonEmpty.await();
                } catch (InterruptedException e) {

                }
            }
            T returnItem = items[head % items.length];
            head++;
            if(tail - head ==items.length-1){
                nonFull.signal();
            }
            return returnItem;
        }
        finally {
            headLock.unlock();
        }
    }
}
