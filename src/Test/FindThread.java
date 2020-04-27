package Test;

import ConcurrentHashTable.ConcurrentHashTable;

public class FindThread<K, V> implements Runnable {

    private K[] keySet;
    private ConcurrentHashTable<K, V> map;

    FindThread(K[] keySet, ConcurrentHashTable<K, V> map) {
        this.keySet = keySet;
        this.map = map;
    }

    @Override
    public void run() {
        for (int i = 0; i < keySet.length; i++) {
            map.get(keySet[i]);
        }
    }
}
