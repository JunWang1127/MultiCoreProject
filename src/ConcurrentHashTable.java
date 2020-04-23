/**
 * the interface for concurrent hash tables
 */
public interface ConcurrentHashTable<K, V> {

    public int hash(final K key,final V value);

    public Boolean put(final K key, final V value);

    public V get(final K key);
}
