import java.util.concurrent.atomic.AtomicInteger;

public class LockFreeBoundedQueue<T> {

    private T[] items;

    private AtomicInteger head = new AtomicInteger(0);
    private AtomicInteger tail = new AtomicInteger(0);
    private AtomicInteger tailCommit = new AtomicInteger(0);
    private AtomicInteger cap;

    public LockFreeBoundedQueue(int cap){
        this.items = (T[]) new Object[cap];
        this.cap = new AtomicInteger(cap);
    }

    public void enqueue(T item){
        int latentCap = cap.get();
        while (latentCap <= 0 || !cap.compareAndSet(latentCap, latentCap - 1)) {
            latentCap = cap.get();
        }
        int i = this.tail.getAndIncrement();
        this.items[i % this.items.length] = item;

        while(this.tailCommit.compareAndSet(i,i+1)){

        };

    }

    public T dequeue(){
        int i = this.head.getAndIncrement();
        while(i>=this.tailCommit.get()){};
        T item = items[i % this.items.length];
        cap.incrementAndGet();

        return item;
    }
}
