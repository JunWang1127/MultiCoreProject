package Test;

import ConcurrentHashTable.ConcurrentHashTable;

import java.util.Map;

public class FindThread<K, V> implements Runnable {

    private K[] keySet;
    private ConcurrentHashTable<K, V> map;
    private Map<K, V> sdkMap = null;

    public FindThread(K[] keySet, ConcurrentHashTable<K, V> map) {
        this.keySet = keySet;
        this.map = map;
    }

    public FindThread(K[] keySet, Map<K, V> map) {
        this.keySet = keySet;
        this.sdkMap = map;
    }

    @Override
    public void run() {
        if(sdkMap == null){
            for (int i = 0; i < keySet.length; i++) {
                map.get(keySet[i]);
            }
        }else{
            for (int i = 0; i < keySet.length; i++) {
                sdkMap.get(keySet[i]);
            }
        }
    }
}
