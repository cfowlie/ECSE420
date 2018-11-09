package com.ecse429;

import java.util.concurrent.locks.Lock;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int nThreads = 5;

        Thread[] pool = new Thread[nThreads];

        for (int i = 0; i < nThreads; i++) {
            pool[i] = new Thread(new SampleThread(i));
        }
        for (int i = 0; i < nThreads; i++) {
            pool[i].start();
        }
        for (int i = 0; i < nThreads; i++) {
            pool[i].join();
        }

    }
}

class SampleThread implements Runnable{

    private static Lock lock;
    private static int threadID;
    boolean critical;

    SampleThread(int threadID){
        super();

        this.threadID = threadID;

        /* Toggle to switch lock type */

        lock = new FilterLock(5, threadID);
        //lock = new BakeryLock(5);

        // No thread starts in critical section
        critical = false;
    }

    @Override
    public void run() {

        lock.lock();

        if(critical){
            System.out.println("Mutual Exclusion Failed");
            System.exit(1);
        }

        critical = true;

        System.out.println(String.format("Thread %d in critical section", threadID));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {}

        critical = false;
        lock.unlock();
    }

}