package com.ecse429;

import java.awt.*;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class BakeryLock implements Lock {

    AtomicBoolean[] flag;
    AtomicInteger[] label;
    private final int n;

    public BakeryLock(int n){
        this.n = n;
        flag = new AtomicBoolean[n];
        label = new AtomicInteger[n];
        for (int i = 0; i < n; i++) {
            flag[i] = new AtomicBoolean();
            label[i] = new AtomicInteger();
        }
    }

    public int currentThreadID(){
        String name = Thread.currentThread().getName();
        int threadID = Integer.parseInt(name.split("-")[1]);
        return threadID;
    }

    @Override
    public void lock() {
        flag[currentThreadID()] = new AtomicBoolean(true);
        AtomicInteger max = new AtomicInteger();

        for (AtomicInteger atomicInteger : label) {
            max = new AtomicInteger(Math.max(atomicInteger.get(), max.get()));
        }
        max.incrementAndGet();

        label[currentThreadID()] = max;

        for(int k = 0; k < n ; k++)
            while((k!=currentThreadID()) && flag[k].get() && (label[k].get() < label[currentThreadID()].get()
                    || (label[k].get() == label[currentThreadID()].get() && k < currentThreadID())));
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

        flag[threadID] = new AtomicBoolean();
    }

    @Override
    public Condition newCondition() {
        return null;
    }

}
