package Test;

import ConcurrentHashTable.ConcurrentHashTable;
import LockBased.LockBasedHashTable;
import LockFree.LockFreeHashTable;
import PhaseConcurrent.PhaseConcurrentHashTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Jun Wang
 * this class for compare the time cost for each concurrent hash table in the same operation set
 */
public class TestTime {

    private static final int TEST_SIZE = 1000000;
    private static final String[] TES_KEY_SET = new String[TEST_SIZE];
    private static final Integer[] TES_VALUE_SET = new Integer[TEST_SIZE];

    public static void main(String[] args) {

        Random rand = new Random();

        for (int i = 0; i <  TEST_SIZE; i++) {
            int keyNum = rand.nextInt(10000);
            TES_KEY_SET[i] = "the key for #" + keyNum;
            TES_VALUE_SET[i] = i;
        }

        //test the time consume for input operation
        TestCase1(1);
        TestCase1(2);
        TestCase1(4);
        TestCase1(6);
        TestCase1(8);

        //test the time consume for remove operation
        TestCase2(1);
        TestCase2(2);
        TestCase2(4);
        TestCase2(6);
        TestCase2(8);

        //test the time consume for find operation
        TestCase3(1);
        TestCase3(2);
        TestCase3(4);
        TestCase3(6);
        TestCase3(8);

        //test the time consume for java sdk map for put, find, remove operation
        TestSDKMap(1);
        TestSDKMap(2);
        TestSDKMap(4);
        TestSDKMap(6);
        TestSDKMap(8);



    }

    /**
     * test the put operation for concurrent hash tables
     * test case 1 is for only put operations
     */
    private static void TestCase1(int numThreads){

        int work = TEST_SIZE/numThreads;

        List<ConcurrentHashTable<String, Integer>> mapList = new ArrayList<>();
        // because this map using priority probing hash, the slot must bigger than test size
        mapList.add(new PhaseConcurrentHashTable<>(TEST_SIZE+100));
        mapList.add(new LockFreeHashTable<>(TEST_SIZE/10));
        mapList.add(new LockBasedHashTable<>());

        for(ConcurrentHashTable<String, Integer> table : mapList){

            Thread[] threads = new Thread[numThreads];
            for(int i = 0; i< numThreads; i++){
                threads[i] = new Thread(new PutThread<String, Integer>(
                        getSliceOfArray(TES_KEY_SET, i*work,(i+1)*work),
                        getSliceOfArray(TES_VALUE_SET,i*work,(i+1)*work),
                        table));
            }


            long startTime = System.nanoTime();

            for(int i = 0; i< numThreads; i++){
                threads[i].start();
            }

            for (Thread thread : threads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            long expected = System.nanoTime() - startTime;
            double seconds = (double)expected / 1_000_000_000.0;
            System.out.println("TEST CASE 1: Runtime for " + numThreads + " threads is: " + seconds + "s");
        }
    }


    /**
     * test the remove operation for concurrent hash tables
     * test case 2 is for only remove operations
     */
    private static void TestCase2(int numThreads){

        int work = TEST_SIZE/numThreads;

        List<ConcurrentHashTable<String, Integer>> mapList = new ArrayList<>();
        // because this map using priority probing hash, the slot must bigger than test size
        mapList.add(new PhaseConcurrentHashTable<>(TEST_SIZE+100));
        mapList.add(new LockFreeHashTable<>(TEST_SIZE/10));
        mapList.add(new LockBasedHashTable<>());

        // pre put elements into map
        for(ConcurrentHashTable<String, Integer> table : mapList){

            Thread initial = new Thread(new PutThread<String, Integer>(TES_KEY_SET, TES_VALUE_SET, table));
            initial.start();
            try {
                initial.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // start remove concurrently
        for(ConcurrentHashTable<String, Integer> table : mapList){

            Thread[] threads = new Thread[numThreads];
            for(int i = 0; i< numThreads; i++){
                threads[i] = new Thread(new PhaseRemoveThread<String, Integer>(
                        getSliceOfArray(TES_KEY_SET, i*work,(i+1)*work),
                        getSliceOfArray(TES_VALUE_SET,i*work,(i+1)*work),
                        table));
            }


            long startTime = System.nanoTime();

            for(int i = 0; i< numThreads; i++){
                threads[i].start();
            }

            for (Thread thread : threads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            long expected = System.nanoTime() - startTime;
            double seconds = (double)expected / 1_000_000_000.0;
            System.out.println("TEST CASE 2: Runtime for " + numThreads + " threads is: " + seconds + "s");
        }
    }


    /**
     * test the find operation for concurrent hash tables
     * test case 3 is for only find operations
     */
    private static void TestCase3(int numThreads){

        int work = TEST_SIZE/numThreads;

        List<ConcurrentHashTable<String, Integer>> mapList = new ArrayList<>();
        // because this map using priority probing hash, the slot must bigger than test size
        mapList.add(new PhaseConcurrentHashTable<>(TEST_SIZE+100));
        mapList.add(new LockFreeHashTable<>(TEST_SIZE/10));
        mapList.add(new LockBasedHashTable<>());

        // pre put elements into map
        for(ConcurrentHashTable<String, Integer> table : mapList){

            Thread initial = new Thread(new PutThread<String, Integer>(TES_KEY_SET, TES_VALUE_SET, table));
            initial.start();
            try {
                initial.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // start remove concurrently
        for(ConcurrentHashTable<String, Integer> table : mapList){

            Thread[] threads = new Thread[numThreads];
            for(int i = 0; i< numThreads; i++){
                threads[i] = new Thread(new FindThread<String, Integer>(
                        getSliceOfArray(TES_KEY_SET, i*work,(i+1)*work),
                        table));
            }


            long startTime = System.nanoTime();

            for(int i = 0; i< numThreads; i++){
                threads[i].start();
            }

            for (Thread thread : threads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            long expected = System.nanoTime() - startTime;
            double seconds = (double)expected / 1_000_000_000.0;
            System.out.println("TEST CASE 3: Runtime for " + numThreads + " threads is: " + seconds + "s");
        }
    }

    /**
     * test the put and remove operation for java sdk concurrent hash map
     */
    private static void TestSDKMap(int numThreads){
        // test put operation
        int work = TEST_SIZE/numThreads;
        Map<String, Integer> sdkMap = new ConcurrentHashMap<>();
        Thread[] threads = new Thread[numThreads];
        for(int i = 0; i< numThreads; i++){
            threads[i] = new Thread(new PutThread<String, Integer>(
                    getSliceOfArray(TES_KEY_SET, i*work,(i+1)*work),
                    getSliceOfArray(TES_VALUE_SET,i*work,(i+1)*work),
                    sdkMap));
        }


        long startTime = System.nanoTime();

        for(int i = 0; i< numThreads; i++){
            threads[i].start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long expected = System.nanoTime() - startTime;
        double seconds = (double)expected / 1_000_000_000.0;
        System.out.println("TEST Put for sdk map: Runtime for " + numThreads + " threads is: " + seconds + "s");

        // test find time
        for(int i = 0; i< numThreads; i++){
            threads[i] = new Thread(new FindThread<String, Integer>(
                    getSliceOfArray(TES_KEY_SET, i*work,(i+1)*work),
                    sdkMap));
        }


        startTime = System.nanoTime();

        for(int i = 0; i< numThreads; i++){
            threads[i].start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        expected = System.nanoTime() - startTime;
        seconds = (double)expected / 1_000_000_000.0;
        System.out.println("TEST find for sdk map: Runtime for " + numThreads + " threads is: " + seconds + "s");

        // test remove time
        for(int i = 0; i< numThreads; i++){
            threads[i] = new Thread(new PhaseRemoveThread<String, Integer>(
                    getSliceOfArray(TES_KEY_SET, i*work,(i+1)*work),
                    getSliceOfArray(TES_VALUE_SET,i*work,(i+1)*work),
                    sdkMap));
        }


        startTime = System.nanoTime();

        for(int i = 0; i< numThreads; i++){
            threads[i].start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        expected = System.nanoTime() - startTime;
        seconds = (double)expected / 1_000_000_000.0;
        System.out.println("TEST remove for sdk map: Runtime for " + numThreads + " threads is: " + seconds + "s");

    }


    private static Integer[] getSliceOfArray(Integer[] arr, int start, int end)
    {

        // Get the slice of the Array
        Integer[] slice = new Integer[end - start];

        // Copy elements of arr to slice
        for (int i = 0; i < slice.length; i++) {
            slice[i] = arr[start + i];
        }

        // return the slice
        return slice;
    }

    private static String[] getSliceOfArray(String[] arr, int start, int end)
    {

        // Get the slice of the Array
        String[] slice = new String[end - start];

        // Copy elements of arr to slice
        for (int i = 0; i < slice.length; i++) {
            slice[i] = arr[start + i];
        }

        // return the slice
        return slice;
    }
}
