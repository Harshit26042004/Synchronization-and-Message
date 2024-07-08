# Synchronization-and-Message

### PROBLEM STATEMENT

Given a shared memory which can be accessed by mulitple users.This shared memory is considered as ticket details available for a ticket reservation system with concurrent process.The consistency of data should be maintained after multiple purchases and refunds.

### SOLUTION

One **shared memory** is accessed by **multiple users**.Therefore multiple users will be available with n number of ticket counter.

These shared memory is dynamically accessed and modified by multiple users.

**Multithreading** can be used to achieve the process by **multiple customers at concurrent time**.

### Without synchronization
![without synchronization](https://github.com/Harshit26042004/Synchronization-and-Message/blob/main/without_sync.jpg)

Consistent of data is one of the crucial matter involved in concurrent processing.

| **Thread-1** | **Thread-2** | **Shared Memory** |
| --- | --- | --- |
| 100-1 = 99 | 100-1 = 99 | 100 |
| 100-1 = 99 | 100-1 = 99 | 100 |
| 100-1 = 99 | 100-1 = 99 | 100 |
| **Final shared memory** |     | **99** |

This problem can be solved with the help of **concurrency control** mechanisms.So that threads work in **synchronized manner**.

That is after completing a full process of a thread , next thread is executed.

### With synchronization
![with synchronization](https://github.com/Harshit26042004/Synchronization-and-Message/blob/main/with_sync.jpg)

For example:

| **Thread-1** | **Thread-2** | **Shared Memory** |
| --- | --- | --- |
| 100-1 = 99 | 100-1 = 99 | 100 |
| 99-1 = 98 | 99-1 = 98 | 99  |
| 98-1 = 97 | 98-1 = 97 | 98  |
| **Final shared memory** |     | **97** |

With java,multithreading is achieved to make a concurrent process and consistency is achieved for each transactions.
<hr>

### PROBLEM STATEMENT

Polling is a concept of frequent checking of process completion which is not a efficient mechanism.It can be solved with notification from producer to consumer.There are 3 patterns…

1. **BroadCast**
2. **Atleast N**
3. **Atmost N**

### SOLUTION

These 3 Patterns can be written as a custom protocol like two phase commit.

**Two Phase commit** is a protocol where there are 2 states

1. **Prepare state** - used to check whether each can commit.
2. **Commit state** - when prepare state gives yes it commits otherwise it aborts transaction.

These patterns are made as methods of a MessageProtocol class which can use based on needs.

When all subscriber need to receive message BroadCast() is used.

### Broadcast() with atleast n method as false
![atleast_n](https://github.com/Harshit26042004/Synchronization-and-Message/blob/main/atleast_n.jpg)

When atleast n active subscriber need to receive else no one can receive is sendAtleastN().

When atmost n active subscriber need to receive else no one can receive is sendNeverMoreThanN().

### Atmost n method as false
![atmost_n](https://github.com/Harshit26042004/Synchronization-and-Message/blob/main/atmost_n.jpg)

These protocol is **functioned with producer** who sends the message and Thread is created for MessageSubscriber.

This **works by thread** and processed and in concurrent way.To simulate the working,the thread is started with run() with all three patterns.
