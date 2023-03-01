public interface HashTableInterface<K, V> {
    public void insert(K key, V value);
    public boolean search(K key);
    public long hashCode(V key);
}
