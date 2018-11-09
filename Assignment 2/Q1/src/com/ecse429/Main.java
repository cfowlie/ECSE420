package com.ecse429;

import java.util.concurrent.locks.Lock;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int nThreads = 5;

        Thread[] pool = new Thread[nThreads];

        for (Thread thread : pool) {
            thread = new Thread(new SampleThread());
            thread.start();
            thread.join();
        }

    }
}

class SampleThread implements Runnable{

    private static Lock lock;

    SampleThread(){
        super();
        lock = new FilterLock(5);
        //lock = new BakeryLock(5);
    }

    @Override
    public void run() {
        long id = Thread.currentThread().getId();

        lock.lock();

        lock.unlock();
    }

}