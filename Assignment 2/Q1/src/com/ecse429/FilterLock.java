package com.ecse429;


import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class FilterLock implements Lock {

    int threadID;
    AtomicInteger[] level;
    AtomicInteger[] victim;
    private final int n;

    public FilterLock(int n, int threadID) {
        this.threadID = threadID;
        this.n = n;

        level = new AtomicInteger[n];
        victim = new AtomicInteger[n]; // use 1..n-1 for (int i = 0; i < n; i++)
        for (int i = 0; i < n; i++) {
            level[i] = new AtomicInteger();
            victim[i] = new AtomicInteger();
        }
    }

    @Override
    public void lock() {
        for (int i = 1; i < n; i++) { // attempt level i
            level[threadID] = new AtomicInteger(i);
            victim[i] = new AtomicInteger(threadID);
            // spin while conflicts exist
            for(int k = 0; k < n ; k++)
                while ((k != threadID) && (level[k].get() >= i && victim[i].get() == threadID)){};

        }
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {
        level[threadID] = new AtomicInteger();
    }

    @Override
    public Condition newCondition() {
        return null;
    }

}