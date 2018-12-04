# ECSE 420 Assignment 3

## Antoine Khouri 260683888 & Connor Fowlie 260687955

## Question 1

1.1) L' is the cache size. t0 is the time to fetch a cache hit. If the entire array can fit in the cache, time to fetch a hit will be constant since they are all cache hits.

1.2) t1 indicates time to fetch for a cache miss.

1.3) 1: The time to fetch a cache hit is constant. see 1.1.

2: as the stride increases, the less elements we can have in our cache, meaning cache misses become more and more likely, which increases the average access time.

3: Once the stride is larger than the cache, then every single memory is a cache miss, which is why average memory access time has a ceiling of t1.

1.4)

## Question 2

2.1) See FineGrainedLock.java, more specifically the contains() method in there.

2.2) See fineGrainedLockTest.java and run it. You can also play around with the tested values if you would like.

The reason our code works is very simple. We know our FineGrainedLock list is sorted in ascending hashcode ("key" in Node()) order. So what we do is iterate up until our currNode's key is no longer smaller to that of the item that we are checking for. Logically speaking, if the next currNode's key is not equal to that of the item we are checking for, the neither will be any of the future keys, as they are sorted in ascending order. Finally, since we are locking and unlocking appropriately, there will never be issues such as wrong values being read. Therefore, our code works just fine.

## Question 3

3.1) See BoundedQueueLock.java.

3.2) See LockFreeBoundedQueue. We ran into difficulty when trying to choose the valid indices to be read or written. Adding two atomic variables, one representing the space left in our queue for our items to be written into, the other to represent the indices that can be validly read.

## Question 4

4.1) See SequentialMatrixVectorMultiply.java.

4.2) See ParallelMatrixVectorMultiply.java.

4.3) See MatrixMultiplyTest.java. For the Parellel operation, there were 32 threads used. The runtime we got was 8.576 seconds for Sequential, and 0.397 for Parallel execution. That is a speedup of 8.576/0.397=21.60.

4.4)