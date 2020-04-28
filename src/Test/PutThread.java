package Test;

import ConcurrentHashTable.ConcurrentHashTable;

import java.util.Map;

public class PutThread<K, V> implements Runnable {

    private K[] keySet;
    private V[] valueSet;
    private ConcurrentHashTable<K, V> map;
    private Map<K,V> sdkMap = null;

    public PutThread(K[] keySet, V[] valueSet, ConcurrentHashTable<K, V> map) {
        this.keySet = keySet;
        this.valueSet = valueSet;
        this.map = map;
    }

    public PutThread(K[] keySet, V[] valueSet, Map<K, V> map) {
        this.keySet = keySet;
        this.valueSet = valueSet;
        this.sdkMap = map;
    }

    @Override
    public void run() {
        if(sdkMap == null) {
            for(int i = 0; i < keySet.length; i++){
                map.put(keySet[i], valueSet[i]);
            }
        }else {
            for(int i = 0; i < keySet.length; i++){
                sdkMap.put(keySet[i], valueSet[i]);
            }
        }
    }
}