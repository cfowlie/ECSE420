package com.ecse429;

import java.util.concurrent.locks.Lock;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int nThreads = 10;

        Thread[] pool = new SampleThread[nThreads];

        for (int i = 0; i < nThreads; i++) {
            pool[i] = new SampleThread();
        }

        for (int j = 0; j < nThreads; j++) {
            pool[j].start();
        }

        for (int k = 0; k < nThreads; k++) {
            pool[k].join();
        }

    }
}

class SampleThread extends Thread implements Runnable{

    /* Toggle to switch lock type */
//    private static final FilterLock lock = new FilterLock(10);
    private static final BakeryLock lock = new BakeryLock(10);

    static boolean critical = false;

    SampleThread(){
        super();
    }

    @Override
    public void run() {

        lock.lock();

        if(critical){ // If any ofther thread is in critical, mutex failed
            System.out.println("Mutual Exclusion Failed");
            System.exit(1);
        }

        critical = true;

        System.out.println(String.format("Thread %d in critical section", lock.currentThreadID()));

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {}

        critical = false;

        lock.unlock();
    }

}