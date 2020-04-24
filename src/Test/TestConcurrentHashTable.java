package Test;

import ConcurrentHashTable.ConcurrentHashTable;
import org.junit.Assert;
import org.junit.Test;

public class TestConcurrentHashTable {

    ConcurrentHashTable concurrentHashTable;

    @Test
    public void testCoarseGrainedListSet() {

    }


//    private void makeThread(ConcurrentHashTable list) {
//        Thread[] threads = new Thread[3];
//        threads[0] = new Thread(new PutThread(0, 2000, list));
//        threads[1] = new Thread(new PutThread(0, 3000, list));
//        threads[2] = new Thread(new PutThread(1000, 3000, list));
//        threads[1].start(); threads[0].start(); threads[2].start();
//
//        for (Thread thread : threads) {
//            try {
//                thread.join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}
