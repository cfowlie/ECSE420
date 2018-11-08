# ECSE420 Assignment 2
### Connor Fowlie 260687955 & Antoine Khouri 260683888

## Question 1
# TODO

1.2)

1.4)

1.5)

## Question 2
In order to answer the question, we must first define the difference between atomic registers & regular registers. In an atomic register, read() will always return the last written value. In a regular register however, if a read overlaps a write then it may return that written value, or possibly the previous one.

 
Fig 1. LockOne algorithm


As we can see, flag is only read() read once in LockOne is on line 8, in the while loop's condition. Suppose there are two threads, A and B running. A is stuck in the while loop, and an overlap occurs in thread B on line 7. We then have two cases if we're using regular registers:

A-	Thread A reads the old value of flag[j]: true and remains in the loop

B-	Thread A reads the new value of flag[j]: true and remains in the loop.


Fig 2. LockTwo algorithm

Once again, we can see that victim is only read() once - on line 6. Suppose again we have two threads A and B, with thread A already in the while loop and an overlap occurs in thread B on line 5. We then again have two cases:

A-	Thread A reads the old value of victim: ThreadA and remains in the loop, allowing B to enter its critical section.

B-	Thread A reads the new value of victim: ThreadB and enter its own critical section while B is in the while loop.

So after analyzing the situation, we can deduce that LockOne and LockTwo do indeed satisfy two-thread mutual exclusion even if "flag" and "victim" are replaced with regular registers.`

Reference:

The art of multiprocessor programming, Maurice Herlihy, Nir Shavit.
(LockOne and LockTwo algorithms).

## Question 3

3.1) Suppose two threads, A and B, enter their critical sections at the same time, with A being the one that arrived first.
When A arrived, "busy" must have been set to true. When B arrives, turn is set to B, and since busy is true, B will be stuck
in its while loop. Now, in order for B to have entered its critical section, then busy must have been set to false at some
point. In order for busy to have been set to false, A or B must have
called unlock(), contradicting our original assumption, which satisfies mutual exclusion.

# 3.2)  TODO

3.3) Assume multiple threads are stuck in the wile loop on line 9, while another thread A is in its critical section. When
thread A exits its critical section and unlocks, the thread that most recently set turn to be itself will be the one to enter
its critical section next, creating potential starvation since there's no ordered way to select the next thread to enter
its critical section.

## Question 4

-fig 2a. is sequentially consistent. Indeed, the sequential execution write(0), write(1), read(1), write(2), read(2), read(2), write(3) satisfies the requirements to be sequentially consistent.

-fig 2b  is not sequentially consistent. Assume it was. In order to read(2), write(1) must occur sometime before read(2) and write(2) must occur sometime between write(1) and read(2). So the order needs to be (i) write(1)->write(2)->read(2). In order to read(1), write(2) must occur some time before read(1) and write(1) must occur some time between write(2) and read(1). So the order needs to be (ii) write(2)->write(1)->read(1). Clearly (i) and (ii) contradict each other, meaning the history is not sequentially consistent.

-Neither histories are linearizable

# TODO add explanation

## Question 5
# TODO

5.1)

5.2)

## Question 6
# TODO

6.1)

6.2)

## Question 7
# TODO

## Question 8
# TODO