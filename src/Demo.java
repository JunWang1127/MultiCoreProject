import ConcurrentHashTable.ConcurrentHashTable;
import LockFree.LockFreeHashMap;

public class Demo {
    public static void main(String[] args) {

        ConcurrentHashTable<String, String> c = new LockFreeHashMap<>();

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

        System.out.println(c.get("jun"));
    }
}
