# MultiCoreProject
## Team Members: Jun Wang, Xiyu Wang

## plot
this folder contains the plot image of the run time experiment results.

## Time Plot.ipynb
a notebook python file for plotting our experiment results.  

## src/ConcurrentHashTable
this package contains the interface for concurrent hash table.

**[ConcurrentHashTable.java](src/ConcurrentHashTable/ConcurrentHashTable.java)** is an interface that for our concurrent hash tables. It contains all methods the tables should have.

## src/LockBased
this package contains implementation of two types of lock-based hash table

1. **[CuckooLockBasedHashTable.java](src/LockBased/CuckooLockBasedHashTable.java)** is the lock-based implementation for Cuckoo hash algorithm.

2. **[LockBasedHashTable.java](src/LockBased/LockBasedHashTable.java)** is the lock-based implementation for segment chain hash algorithm.

## src/LockFree
this package contains implementation of one of lock-based hash table

1. **[LockFreeHashTable.java](src/LockFree/LockFreeHashTable.java)** is the lock-free implementation for chain hash algorithm using CAS.

2. **[Node.java](src/LockFree/Node.java)** is the node class of chain for lock-free hash table.

## src/PhaseConcurrent
this package contains implementation of another of lock-free hash table that using phase concurrent

1. **[PhaseConcurrentHashTable.java](src/PhaseConcurrent/PhaseConcurrentHashTable.java)** is the implementation of phase concurrent hash table with liner probing algorithm.

2. **[MapEntry.java](src/PhaseConcurrent/MapEntry.java)** is the map entry data structure in slot for phase concurrent hash table.

## src/Test
This package contains unit test and an TestTime class for comparing the performance between Java JDK concurrent map and our tables

1. **[FindThread.java](src/Test/FindThread.java), [PhaseRemoveThread.java](src/Test/PhaseRemoveThread.java), [PutThread.java](src/Test/PutThread.java), [RemoveThread.java](src/Test/RemoveThread.java)**
are the threads classes for test table in concurrent environment. Each thread will have it own operation. The put thread will put a set of entry into table
, The Remove thread will put a set of entries first and then remove them from table. The Phase Remove just remove a set of entries from table. Find Thread will find entries.

2. **[TestCuckooLockBasedHashTable.java](src/Test/TestCuckooLockBasedHashTable.java), [TestLockBasedHashTable.java](src/Test/TestLockBasedHashTable.java)**
**, [TestLockFreeHashTable.java](src/Test/TestLockFreeHashTable.java), [TestPhaseConcurrentHashTable.java](src/Test/TestPhaseConcurrentHashTable.java)** are 
Junit tests for these tables.

3. **[TestTime.java](src/Test/TestTime.java)** is a class to compare performance between our tables with concurrent hash map provided by JDK1.8

## How to run this project

First you should import whole project into IDE and import the library. The necessary library **hamcrest-core-1.3.jar** and **junit-4.12.jar** 
are included in this project.

1. There is a  **[Demo.java](src/Demo.java)** class you can run directly. This class is just a simple example that run put and remove concurrently.

2. You can also run **[TestTime.java](src/Test/TestTime.java)** to test all hash tables in this project with different number of threads in different kinds of operations.

3. Junit tests are also ready for you to run tests 

4. If you want to use our tables in your program, just declare in this format (use lock-free table for example) **ConcurrentHashTable<KeyType, ValueType> table = new LockFreeHashTable<>(Initial Size);**
and use it as JDK's hash table.

