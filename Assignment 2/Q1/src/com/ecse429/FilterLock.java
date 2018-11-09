package com.ecse429;


import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class FilterLock implements Lock {

    AtomicInteger[] level;
    AtomicInteger[] victim;
    private final int n;

    public FilterLock(int n) {
        this.n = n;
        level = new AtomicInteger[n];
        victim = new AtomicInteger[n]; // use 1..n-1 for (int i = 0; i < n; i++)
        for (int i = 0; i < n; i++) {
            level[i] = new AtomicInteger();
            victim[i] = new AtomicInteger();
        }
    }

    public int currentThreadID(){
        String name = Thread.currentThread().getName();
        int threadID = Integer.parseInt(name.split("-")[1]);
        return threadID;
    }

    @Override
    public void lock() {
        for (int i = 1; i < n; i++) { // attempt level i
            level[currentThreadID()] = new AtomicInteger(i);
            victim[i] = new AtomicInteger(currentThreadID());
            // spin while conflicts exist
            for(int k = 0; k < n ; k++)
                while ((k != currentThreadID()) && (level[k].get() >= i && victim[i].get() == currentThreadID()));

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
        String name = Thread.currentThread().getName();
        int threadID = Integer.parseInt(name.split("-")[1]);

        level[threadID] = new AtomicInteger();
    }

    @Override
    public Condition newCondition() {
        return null;
    }



}