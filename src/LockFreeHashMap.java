import LockFree.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class LockFreeHashMap<K, V> implements ConcurrentHashTable<K, V> {

    private List<Node<K, V>> buckets;
    private int numBuckets;
    private AtomicInteger size;


    public LockFreeHashMap() {
        this.size = new AtomicInteger(0);
        this.numBuckets = 20;
        this.buckets = new ArrayList<>();

        //initialize the buckets with heads
        for (int i = 0; i < this.numBuckets; i++) {
            this.buckets.add(new Node<>());
        }
    }

    @Override
    public int hash(K key) {
        return key.hashCode() % this.numBuckets;
    }

    @Override
    public V put(K key, V value) {
        int hash = hash(key);
        return addNode(key, value, buckets.get(hash));
    }

    @Override
    public V get(K key) {
        int hash = hash(key);
        Node<K,V> find = buckets.get(hash).next.getReference();
        int priority = key.hashCode();

        while(find != null){
            if(priority < find.priority){
                return null;
            }

            if(find.key.equals(key)){
                if(find.isDeleted.get()){
                    return null;
                }else{
                    return find.value;
                }
            }

            find = find.next.getReference();
        }

        return null;
    }

    @Override
    public V remove(K key) {
        return null;
    }

    @Override
    public boolean remove(K key, V value) {
        return false;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean containsKey(K key) {
        return false;
    }

    private V addNode(K key, V value, Node<K, V> head) {

        Node<K, V> node = new Node<>(key, value);

        // the different object may have same value, thus we also has to compare value
        int priority = node.priority;

        while (true) {
            Node<K, V> pre = head;
            Node<K, V> checkIfExist = head.next.getReference(); // check if the key already exits

            while (checkIfExist != null) {
                if (checkIfExist.key.equals(key) && !checkIfExist.isDeleted.get()) {
                    // if exist, just overwrite the value of this node
                    V oldValue = checkIfExist.value;
                    checkIfExist.value = value;

                    return oldValue;
                }
                checkIfExist = checkIfExist.next.getReference();
            }

            // find a right position based on priority of value
            while (pre.next.getReference() != null && pre.next.getReference().priority < priority) {
                pre = pre.next.getReference();
            }

            // get stamp of pre node's next pointer
            int stamp = pre.next.getStamp();
            Node<K, V> successor = pre.next.getReference();
            node.next.set(successor, 0);

            // if pre is not deleted and point to right successor
            if (!pre.isDeleted.get()) {
                // use stamped to avoid ABA
                if (pre.next.compareAndSet(successor, node, stamp, stamp + 1)) {
                    //update the size of hashMap
                    this.size.incrementAndGet();

                    return null;
                }
            }
        }
    }


}
