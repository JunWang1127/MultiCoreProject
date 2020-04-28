import ConcurrentHashTable.ConcurrentHashTable;
import LockFree.LockFreeHashTable;
import Test.PutThread;
import Test.RemoveThread;

public class Demo {

    public static void main(String[] args) throws InterruptedException {

        ConcurrentHashTable<String, String> table = new LockFreeHashTable<>(2);

        Thread[] threads = new Thread[3];
        threads[0] = new Thread(new PutThread<String, String>(
                new String[] {"BMW", "Toyota", "Volkswagen", "Skoda" },
                new String[] {"BMW1", "Toyota1", "Volkswagen", "Skoda" }, table));
        threads[1] = new Thread(new PutThread<String, String>(
                new String[] {"BMW", "Toyota", "Mercedes" },
                new String[] {"BMW2", "Toyota2", "Mercedes" }, table));

        threads[2] = new Thread(new RemoveThread<String, String>(
                new String[] {"Volkswagen"},
                new String[] {"Volkswagen"}, table));

        threads[0].start();
        threads[1].start();
        threads[0].join();

        threads[2].start();
        threads[1].join();
        threads[2].join();

        System.out.println(table.toString());

    }
}
