## PROBLEM STATEMENT

Given a shared memory which can be accessed by mulitple users.This shared memory is considered as ticket details available for a ticket reservation system with concurrent process.The consistency of data should be maintained after multiple purchases and refunds.

## SOLUTION

During shared memory accessed by Multiple users, the consistency is affected by **RACE Condition**

A **race condition** occurs when two or more threads can **access shared data and they try to change it at the same time**.

For Example , take below table :

| **Thread-1** | **Thread-2** | **Shared Memory** |
| --- | --- | --- |
| 100-1 = 99 | 100-1 = 99 | 100 |
| 100-1 = 99 | 100-1 = 99 | 100 |
| 100-1 = 99 | 100-1 = 99 | 100 |
| **Final shared memory** |     | **99** |

For example:

Consider the process without concurrency control in multi threading. Lets assume user defined customers is 3 and 5 ticket collectors

All the threads starts to use shared memory and two or more thread modify the same memory.

**Customers - 3**

**Ticket Collectors - 5**

So the process took place will be erroneous.Below screenshot is a example without synchronization which gives wrong output of 101.

### Without synchronization
![without synchronization](https://github.com/Harshit26042004/Synchronization-and-Message/blob/main/without_sync.jpg)

This problem can be solved with the help of **concurrency control** mechanisms.So that threads work in **synchronized manner**.

With java,multithreading is achieved to make a concurrent process and consistency is achieved for each transactions.

## What’s New

**Race condition can be handled by Mutex or Semaphores.**

In java, Synchronized keyword is traditionally used for concurrency control and synchronization.Now **Re-entrant Lock** is a (type of **MUTEX)** used which is **more efficient than traditional synchronized** blocks.

Consider a RACE condition where many **threads starts to compete to run a specific task.**

| **Thread-1** | **Thread-2** | **Shared Memory** |
| --- | --- | --- |
| 100-1 = 99 | 100-1 = 99 | 100 |
| 99-1 = 98 | 99-1 = 98 | 99  |
| 98-1 = 97 | 98-1 = 97 | 98  |
| **Final shared memory** |     | **97** |

By using Re-entrant Lock the shared memory used by particular **thread can be locked until completion**.Only after completing this can be used by other thread.

By using Re-entrant lock the consitency of the shared memory is maintained. RACE condition is avoided in below screenshot with lock gives correct output of 97.

### With synchronization
![with synchronization](https://github.com/Harshit26042004/Synchronization-and-Message/blob/main/with_sync.jpg)

## PROBLEM STATEMENT

Polling is a concept of frequent checking of process completion which is not a efficient mechanism.It can be solved with notification from producer to consumer.There are 3 patterns…

1. **BroadCast**
2. **Atleast N**
3. **Atmost N**

## SOLUTION

These 3 Patterns can be written as a custom protocol like two phase commit.

**Two Phase commit** is a protocol where there are 2 states

1. **Prepare state** - used to check whether each can commit.
2. **Commit state** - when prepare state gives yes it commits otherwise it aborts transaction.

These patterns are made as methods of a MessageProtocol class which can use based on needs.An **Acknowledgement should be received by the server** from subscriber.

When all subscriber with acknowledgement need to receive message BroadCast() is used.

### Broadcast() with atleast n method as false
![atleast_n](https://github.com/Harshit26042004/Synchronization-and-Message/blob/main/atleast_discarded.jpg)

When atleast n active subscriber with acknowledgement need to receive else no one can receive is sendAtleastN().
When atmost n active subscriber with acknowledgement need to receive else no one can receive is sendNeverMoreThanN().

### Atmost n method as false
![atmost_n](https://github.com/Harshit26042004/Synchronization-and-Message/blob/main/atmost_discarded.jpg)

These protocol is **functioned with producer** who sends the message and Thread is created for MessageSubscriber.

This **works by thread** and processed and in concurrent way.To simulate the working,the thread is started with run() with all three patterns.

## What’s new

Previously the subscriber list is fixed in size so cannot be used for real-time dynamic solutions.


### Dynamic list
![dynamic list](https://github.com/Harshit26042004/Synchronization-and-Message/blob/main/list.jpg)

Now the **CopyOnWriteArrayList<>()** is used which is used for multi threading process which **updates the list by taking a copy of the list ,** it will be dynamic and flexible by removing final variables and objects.

Based on the current status of the subscribers, the messages may sent or may not sent.

The **minimum active user and maximum active user can be user-defined** so it is more useful in practical implementation.
