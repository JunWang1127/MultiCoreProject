package Test;

import ConcurrentHashTable.ConcurrentHashTable;
import LockBased.LockBasedHashTable;
import LockFree.LockFreeHashTable;
import PhaseConcurrent.PhaseConcurrentHashTable;

import java.util.ArrayList;
import java.util.List;

/**
 * this class for compare the time cost for each concurrent hash table in the same operation set
 */
public class TestTime {

    private static final int TEST_SIZE = 100000;

    public static void main(String[] args) {
        List<ConcurrentHashTable<Integer, Integer>> mapList = new ArrayList<>();
        // because this map using priority probing hash, the slot must bigger than test size
        mapList.add(new PhaseConcurrentHashTable<>(TEST_SIZE+100));
        mapList.add(new LockFreeHashTable<>(TEST_SIZE/10));
        mapList.add(new LockBasedHashTable<>());


    }

    private static void TestCase1(int numThreads, List<ConcurrentHashTable<Integer, Integer>> mapList){
        long startTime = System.nanoTime();
        long expected = System.nanoTime() - startTime;
        System.out.println("Runtime for Lamport Algorithm for " + numThreads + " threads is: " + expected);
    }
}
