package Test;

import ConcurrentHashTable.ConcurrentHashTable;

import java.util.Map;

public class PhaseRemoveThread<K, V> implements Runnable {

    private K[] keySet;
    private V[] valueSet;
    private ConcurrentHashTable<K, V> map;
    private Map<K, V> sdkMap = null;

    PhaseRemoveThread(K[] keySet, V[] valueSet, ConcurrentHashTable<K, V> map) {
        this.keySet = keySet;
        this.valueSet = valueSet;
        this.map = map;
    }

    PhaseRemoveThread(K[] keySet, V[] valueSet, Map<K, V> map) {
        this.keySet = keySet;
        this.valueSet = valueSet;
        this.sdkMap = map;
    }

    @Override
    public void run() {
        if (sdkMap == null) {
            for (int i = 0; i < keySet.length; i++) {
                map.remove(keySet[i]);
            }
        }else{
            for (int i = 0; i < keySet.length; i++) {
                sdkMap.remove(keySet[i]);
            }
        }
    }
}
