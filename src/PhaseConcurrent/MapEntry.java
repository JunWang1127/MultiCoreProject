package PhaseConcurrent;

public class MapEntry<K,V> {
    public K key;
    public V value;
    public int priority;

    public MapEntry(K key, V value){
        this.key = key;
        this.value = value;
        this.priority = key.hashCode();
    }
}
