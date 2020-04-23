/**
 * the interface for concurrent hash tables
 */
public interface ConcurrentHashTable<K, V> {

    public int hash(K key,V value);

    public Boolean put(V value);

    public V get(K key);
}
