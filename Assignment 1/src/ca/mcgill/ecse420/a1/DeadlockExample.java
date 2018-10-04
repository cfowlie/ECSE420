package ca.mcgill.ecse420.a1;

public class DeadlockExample {

    // Basic example of deadlock when 2 consumers each hold one resource and need two in order to complete

    public static void main(String[] args) {

        Object resourceA = new Object();
        Object resourceB = new Object();

        // Thread 1 requires resources A and B to complete
        Thread t1 = new Thread("Thread 1") {

            public void run() {
                synchronized (resourceA){ // Thread 1 requests resource A
                    System.out.println(Thread.currentThread().getName() + " Has Resource A");
                    synchronized (resourceB){ // Thread 1 requests resource B
                        System.out.println(Thread.currentThread().getName() + " Has Resource B");
                    }
                }
            }


        };


        // Thread 2 requires resources B and A to complete
        Thread t2 = new Thread("Thread 2") {

            public void run() {
                synchronized (resourceB){ // Thread 2 requests resource B
                    System.out.println(Thread.currentThread().getName() + " Has Resource B");
                    synchronized (resourceA){ // Thread 2 requests resource A
                        System.out.println(Thread.currentThread().getName() + " Has Resource A");
                    }
                }
            }


        };

        t1.start();
        t2.start();

        // Both threads hold one resource resulting in deadlock
    }


}
