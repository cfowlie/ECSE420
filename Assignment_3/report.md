# ECSE 420 Assignment 3

## Antoine Khouri 260683888 & Connor Fowlie 260687955

## Question 1

1.1)

1.2)

1.3)

1.4)

## Question 2

2.1) See FineGrainedLock.java, more specifically the contains() method in there.

2.2) See fineGrainedLockTest.java and run it. You can also play around with the tested values if you would like.

The reason our code works is very simple. We know our FineGrainedLock list is sorted in ascending hashcode ("key" in Node()) order. So what we do is iterate up until our currNode's key is no longer smaller to that of the item that we are checking for. Logically speaking, if the next currNode's key is not equal to that of the item we are checking for, the neither will be any of the future keys, as they are sorted in ascending order. Finally, since we are locking and unlocking appropriately, there will never be issues such as wrong values being read. Therefore, our code works just fine.