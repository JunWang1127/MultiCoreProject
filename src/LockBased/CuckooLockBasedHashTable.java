package LockBased;

import java.util.*;
import ConcurrentHashTable.ConcurrentHashTable;

public class CuckooLockBasedHashTable<K, V> implements ConcurrentHashTable<K, V> {
    private int MAXN = 3000;
    static int ver = 3;

    protected static final class Segment {
        protected int count = 0;

        protected synchronized int getCount() {
            return this.count;
        }
        protected synchronized void synch(){}
    }

    static class Entry<K, V> implements Map.Entry<K, V> {
        protected final K key;
        protected volatile V value;
        protected final int hash;

        Entry(int hash, K key, V value) {
            this.value = value;
            this.hash = hash;
            this.key = key;
        }

        public int getHash() {
            return hash;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public V setValue(V value) {
            V old = this.value;
            this.value = value;
            return old;
        }
    }

    public final Segment[] segments = new Segment[32];
    protected transient Entry<K, V>[][] table = (Entry<K, V>[][])new Entry[ver][MAXN];
    private Random r = new Random();
    private int[] counts = new int[ver];

    public CuckooLockBasedHashTable(int size) {
        MAXN = size / 3 + 1;
        for (int i = 0; i < ver; i++) {
            counts[i] = 0;
        }
        for (int i = 0; i < segments.length; i++) {
            segments[i] = new Segment();
        }
    }

    @Override
    public int hash(K key) {
        return Math.abs(key.hashCode() % this.MAXN);
    }

    public int cuckooHash(int function, K key) {
        int num = key.hashCode();
        for (int i = 0; i < function; i++) {
            num /= MAXN;
        }
        return Math.abs(num % MAXN);
        /**
        switch(function) {
            case 0: return Math.abs(key.hashCode() % MAXN);
            case 1: return Math.abs((key.hashCode() / MAXN) % MAXN);
            case 2: return Math.abs((key.hashCode() / MAXN / MAXN) % MAXN);
        }
        return Integer.MIN_VALUE;
         */
    }

    private int[] findPos(K key) {
        for (int i = 0; i < ver; i++) {
            int pos = cuckooHash(i, key);
            if (table[i][pos] != null && table[i][pos].key.equals(key)) {
                int[] found = new int[2];
                found[0] = i;
                found[1] = pos;
                return found;
            }
        }
        return null;
    }

    public V replace(int[] pos, K key, V value) {
        if (pos == null) {
            return null;
        }
        Segment seg = segments[(pos[1] & 0x1F)];
        synchronized (seg) {
            V oldValue = table[pos[0]][pos[1]].value;
            table[pos[0]][pos[1]].value = value;
            return oldValue;
        }
    }


    public V insertHelper(K key, V value) {
        final int COUNT_LIMIT = 100;
        int lastPos = -1;
        K theKey = key;
        V theValue = value;
        for (int count = 0; count < COUNT_LIMIT; count++) {
            //Find a position for insert
            for (int i = 0; i < ver; i++) {
                int hashVal = cuckooHash(i, theKey);
                Segment seg = segments[(hashVal & 0x1F)];
                synchronized (seg) {
                    if (table[i][hashVal] == null) {
                        Entry<K, V> newEntry = new Entry(hashVal, theKey, theValue);
                        table[i][hashVal] = newEntry;
                        counts[i] += 1;
                        return null;
                    }
                }
            }
            //No available position
            int ran = r.nextInt(ver);
            int hashVal = cuckooHash(ran, theKey);
            Segment seg = segments[(hashVal & 0x1F)];
            synchronized (seg) {
                Entry<K, V> oldEntry = table[ran][hashVal];
                Entry<K, V> newEntry = new Entry(hashVal, theKey, theValue);
                table[ran][hashVal] = newEntry;
                theKey = oldEntry.key;
                theValue = oldEntry.value;
            }
        }
        System.out.println("Unable to find a position. Rehash needed.\n");
        return theValue;
    }


    @Override
    public V put(final K key, final V value){
        int[] pos = findPos(key);
        if (pos != null) {
            return replace(pos, key, value);
        }
        insertHelper(key, value);
        return null;
    }

    @Override
    public V get(final K key){
        int[] pos = findPos(key);
        if (pos != null) {
            return table[pos[0]][pos[1]].value;
        }
        return null;
    }

    @Override
    public V remove(K key) {
        int[] pos = findPos(key);
        if (pos != null) {
            Segment seg = segments[(pos[1] & 0x1F)];
            synchronized (seg) {
                V oldValue = table[pos[0]][pos[1]].value;
                table[pos[0]][pos[1]] = null;
                counts[pos[0]] -= 1;
                return oldValue;
            }
        }
        return null;
    }

    @Override
    public boolean remove(K key, V value){
        int[] pos = findPos(key);
        if (pos != null) {
            Segment seg = segments[(pos[1] & 0x1F)];
            synchronized (seg) {
                if (table[pos[0]][pos[1]].value.equals(value)) {
                    table[pos[0]][pos[1]] = null;
                    counts[pos[0]] -= 1;
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int size(){
        int count = 0;
        for (int i = 0; i < ver; i++) {
            count += counts[i];
        }
        return count;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean containsKey(K key){
        return findPos(key) != null;
    }

    @Override
    public V getOrDefault(K key, V defaultValue){
        V value = get(key);
        return value == null ? defaultValue : value;
    }

    @Override
    public Set<K> keySet(){
        Set<K> set = new HashSet<>();
        for (int i = 0; i < ver; i++) {
            for (int j = 0; j < MAXN; j++) {
                if (table[i][j] != null) {
                    set.add(table[i][j].key);
                }
            }
        }
        return set;
    }

    @Override
    public Collection<V> values(){
        Collection<V> collection = new ArrayList<V>();
        for (int i = 0; i < ver; i++) {
            for (int j = 0; j < MAXN; j++) {
                if (table[i][j] != null) {
                    collection.add(table[i][j].value);
                }
            }
        }
        return collection;
    }
}


