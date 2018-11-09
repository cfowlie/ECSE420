package com.ecse429;


import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class FilterLock implements Lock {

    long[] level;
    long[] victim;
    private final int n;

    public FilterLock(int n) {
        this.n = n;
        level = new long[n];
        victim = new long[n]; // use 1..n-1 for (int i = 0; i < n; i++)
        for (int i = 0; i < n; i++) {
            level[i] = 0;
        }
    }

    @Override
    public void lock() {
        long me = Thread.currentThread().getId();
        for (int i = 1; i < n; i++) { // attempt level i
            level[(int)me] = i;
            victim[i] = me;
            // spin while conflicts exist
            for(int k = 0; k < n ; k++)
                while ((k != me) && (level[k] >= i && victim[i] == me)){};

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
        long me = Thread.currentThread().getId();
        level[(int)me] = 0;
    }

    @Override
    public Condition newCondition() {
        return null;
    }

}