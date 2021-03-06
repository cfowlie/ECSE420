# ECSE420 Assignment 2
### Connor Fowlie 260687955 & Antoine Khouri 260683888

## Question 1

1.1) See FilterLock.java within Q1 folder for implementation.

1.2) Within a loop iteration, it is impossible for a thread to overtake another, because that would mean changing "victim". Doing so immediately releases the thread waiting in the while loop.

Now, let A be a thread that just completed the first for loop, i.e completed level[1]. Suppose, because of scheduling, A gets paused. It is then possible for any other thread which has already also completed level[1] to overtake A.

1.3) See BakeryLock.hava within Q1 folder for implementation.

1.4) BakeryLock is a first come first serve protocol, therefore the threads should execute in sequential order and not allow any overtaking. Note that at very high number of threads in our example code (say 100) that overtaking begins to happen in small amounts. This is due to some threads running with incorrect labels as the number of virtual threads greatly exceeds the number of physical threads, and labels are not perfectly syncronized.

1.5) This can be tested by spinning up a pool of threads, and initializing a boolean that tracks if any thread is in a "critical" section. Each thread will lock and then set the shared critical bool to true, execute its critical section (wait) and then unset the critical bool and unlock. If any thread can lock while the shared critical section boolean is set to true (meaning another thread is within its critical section), then the mutual exclusion has failed.

1.6) See Main.java in Q1 for implementation.

## Question 2
In order to answer the question, we must first define the difference between atomic registers & regular registers. In an atomic register, read() will always return the last written value. In a regular register however, if a read overlaps a write then it may return that written value, or possibly the previous one.

 
Fig 1. LockOne algorithm


As we can see, flag is only read() once in LockOne, on line 8, in the while loop's condition. Suppose there are two threads, A and B running. A is stuck in the while loop, and an overlap occurs in thread B on line 7. We then have two cases if we're using regular registers:

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

#### 3.2)  TODO

3.3) Assume multiple threads are stuck in the wile loop on line 9, while another thread A is in its critical section. When
thread A exits its critical section and unlocks, the thread that most recently set turn to be itself will be the one to enter
its critical section next, creating potential starvation since there's no ordered way to select the next thread to enter
its critical section.

## Question 4

### Sequential consistency

-fig 2a. is sequentially consistent. Indeed, the sequential execution write(0), write(1), read(1), write(2), read(2), read(2), write(3) satisfies the requirements to be sequentially consistent.

-fig 2b  is not sequentially consistent. Assume it was. In order to read(2), write(1) must occur sometime before read(2) and write(2) must occur sometime between write(1) and read(2). So the order needs to be (i) write(1)->write(2)->read(2). In order to read(1), write(2) must occur some time before read(1) and write(1) must occur some time between write(2) and read(1). So the order needs to be (ii) write(2)->write(1)->read(1). Clearly (i) and (ii) contradict each other, meaning the history is not sequentially consistent.

### Linearizability

-fig 2a.

If we flatten all 3 lines to a single one, we can see that there does not exist moments where the code would work sequentially. Therefore it is not linearizable.

-fig 2b.

P: Linearizability,

Q: Sequential consistency.

We know P->Q, meaning NOT Q -> NOT P. We have already proved NOT Q, therefore the history in fig 2b is not linearizable.

## Question 5


5.1) It is indeed possible for a division by 0 to occur. Volatile​ ​fields​ ​are​ ​used​ ​for​ ​communicating​ ​state​ ​between​ ​threads.​ ​Value​ ​returned​ ​from​ ​a​ ​​read()​​ ​call​ ​will be​ ​the​ ​​last​​ ​value​ ​written​ ​to​ ​that​ ​volatile​ ​field​ ​by​ ​any​ ​thread.

 The boolean v is volatile, wheres the integer x is not. So if thread A calls writer(), it'll write 42 into x and true into v. However since v is volatile, if B calls reader() before calling writer() itself, then v will be true, and x will be 0, which validates the if condition on line 9, incurring a division by 0. A:writer()->B:reader() would incur the division by 0.

5.2) In both cases division by 0 would be impossible. If both were volatile, then even if thread A calls writer() and B only calls reader(), v will be true and x will be 42.

If neither are volatile, the in order for v to ever be true in a thread that calls reader(), that thread must also have called writer(), setting x to 42 and avoid the division by 0.

## Question 6
### TODO

6.1)

6.2)

## Question 7


P: Binary consensus is impossible for 2 threads.

Q: Binary consensus is impossible for >2 threads.

In order to prove P->Q, we will show that NOT Q -> NOT P.

Assume NOT Q: binary consensus possible for >2 threads.

If it is possible for all threads in a certain set of threads, then it is also possible for any two threads in that set. Hence
NOT Q -> NOT P, or P->Q.

## Question 8

Assume that consensus over k>2 values is possible but binary consensus is impossible. By mapping

[0,​ ​⌈k​ ​choose​ ​2⌉]​ ​→​ ​0

[⌊k​ ​choose​ ​2⌋,​ ​k]​ ​→​ ​1,

we then create a binary consensus, contradicting our proposition "binary consensus is impossible."

Therefore, by contrapositive, consensus over k>2 is impossible if binary consensus is impossible.
