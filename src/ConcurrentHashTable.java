/**
 * the interface for concurrent hash tables.
 */
public interface ConcurrentHashTable<K, V> {

    /**
     * hash function for the hashMap.
     */
    int hash(final K key);

    /**
     * put key value into map. If already contains key in map,
     * will replace the old value by new value.
     *
     * @return V is the previous value associate with key,
     * return null if the key it not in the map previously.
     */
     V put(final K key, final V value);

    /**
     * get value based on input key and value.
     */
     V get(final K key);

    /**
     * remove the key-value entry if the key present in map.
     * @return V is the value of the key.
     */
     V remove(final K key);

    /**
     * remove the key-value entry if the input key-value entry present in map.
     * @return V return true if remove successfully, else return false.
     */
     boolean remove(final K key, final V value);

     int size();

     boolean isEmpty();

     boolean containsKey(final K key);
}
