import ConcurrentHashTable.ConcurrentHashTable;
import LockBased.*;
import LockFree.*;

public class Demo {
    public static void main(String[] args) {

        //ConcurrentHashTable<String, String> c = new LockFreeHashMap<>();
        ConcurrentHashTable<String, String> c = new LockBasedHashTable<>(100);
        boolean b_empty = c.isEmpty();
        c.put("jun","123");
        c.put("jun","321");
        c.put("shuqi","321");
        c.put("xiyu","111");
        c.put("yuesen","000");

        int a = c.size();
        boolean b = c.isEmpty();
        boolean d = c.containsKey("aa");
        boolean e = c.containsKey("shuqi");
        c.remove("shuqi");
        c.remove("jun","123");
        c.remove("jun","321");
        c.remove("haha");

        System.out.println(c.get("xiyu"));
        System.out.println(c.keySet());
        System.out.println(b_empty);
        System.out.println(a);
        System.out.println(b);
        System.out.println(d);
        System.out.println(e);
    }
}
