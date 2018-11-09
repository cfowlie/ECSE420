package com.ecse429;

import java.awt.*;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class BakeryLock implements Lock {

    boolean[] flag;
    long[] label;
    private final int n;

    public BakeryLock(int n){
        this.n = n;
        flag = new boolean[n];
        label = new long[n];
        for (int i = 0; i < n; i++) {
            flag[i] = false;
            label[i] = 0;
        }
    }

    @Override
    public void lock() {
        long me = Thread.currentThread().getId();
        flag[(int)me] = true;
        label[(int)me] = Arrays.stream(label).max().getAsLong() + 1;
        for(int k = 0; k < n ; k++)
            while((k!=me) && flag[k] && (label[k] < label[(int)me] || (label[k] == label[(int)me] && k < me)));
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
        flag[(int) Thread.currentThread().getId()] = false;
    }

    @Override
    public Condition newCondition() {
        return null;
    }
}
