public class Bucket<K, V> {
    private K key;
    private V word; // value
    private int count;
    private String binaryKey;
    private int localDepth;

    public Bucket(K key, V word, int count, String binaryKey, int localDepth) {
        this.key = key;
        this.word = word;
        this.count = count;
        this.binaryKey = binaryKey;
        this.localDepth = localDepth;
    }

    public Bucket() {

    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getWord() {
        return word;
    }

    public void setWord(V word) {
        this.word = word;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getBinaryKey() {
        return binaryKey;
    }

    public void setBinaryKey(String binaryKey) {
        this.binaryKey = binaryKey;
    }

    public int getLocalDepth() {
        return localDepth;
    }

    public void setLocalDepth(int localDepth) {
        this.localDepth = localDepth;
    }

}
