import java.util.ArrayList;

import java.util.LinkedList;
import java.util.List;

public class HashTable<K, V> implements HashTableInterface<K,V> {

    private int capacity;
    private int globalDepth;
    private int DEFAULT_GLOBALDEPTH = 8;
    private double DEFAULT_CAPACITY = Math.pow(2, DEFAULT_GLOBALDEPTH);
    private int bucket_size = 10;
    private LinkedList<Bucket<K, V>>[] hashTable;

    @SuppressWarnings("unchecked")
    public HashTable() {
        this.capacity = (int) DEFAULT_CAPACITY;
        this.globalDepth = DEFAULT_GLOBALDEPTH;
        this.hashTable = new LinkedList[this.capacity];
    }

    public void insert(K key, V value) {
        String binaryKey = convertBinary((long) key);
        int index = locateElement(binaryKey, globalDepth);
        LinkedList<Bucket<K, V>> currentLocation = hashTable[index];

        // if location is null create list and add element to list
        // then insert list to location
        if (currentLocation == null) {
            LinkedList<Bucket<K, V>> hashTableElement = new LinkedList<Bucket<K, V>>();
            hashTableElement.add(new Bucket<K, V>(key, value, 1, binaryKey, globalDepth));
            hashTable[index] = hashTableElement;
        }

        // if location is not null search element
        // if it exists then increment count
        // if it doesn't exist then look size if full or not
        // if it's not full then create new element and put it there.
        // if size is full then look gbdepth - locdepth of first element
        // if difference is equal to 0 then double size and then put element
        // if difference is not equal to 0 then split this location elements and then
        // put new element
        if (currentLocation != null && currentLocation.size() <= bucket_size) {

            Bucket<K, V> searchedElement = isListElementExist(currentLocation, key);
            if (searchedElement != null) {
                searchedElement.setCount(searchedElement.getCount() + 1);
            } else if (searchedElement == null && currentLocation.size() < bucket_size) {
                currentLocation.add(new Bucket<K, V>(key, value, 1, binaryKey, globalDepth));
            } else {
                int localDepthOfBucket = currentLocation.getFirst().getLocalDepth();
                if (globalDepth - localDepthOfBucket == 0) {
                    doubleSize();
                    insert(key, value);
                } else {
                    splitBucket(currentLocation);
                    insert(key, value);
                }
            }
        }

    }

    public boolean search(K key) {

        int i = 0;
        String binaryKey = convertBinary((long) key);
        int index = locateElement(binaryKey, globalDepth);
        while (hashTable[index]!= null &&  hashTable[index].size() > i) {
            Bucket<K, V> searchElement = hashTable[index].get(i);
            if (searchElement.getKey().equals(key)) {

                System.out.println("Key : " + searchElement.getKey());
                System.out.println("Count : " + searchElement.getCount());
                // for writing binary index like global depth size ------------------------
                // for example normally 10011 but global depth 7 then result
                // 0010011 is result.Algorithm normally calculate without zeros at start.
                String binIndex = convertBinary(index);
                if (binIndex.length() < globalDepth) {
                    int difference = globalDepth - binIndex.length();
                    String tempBinIndex = "";
                    for (int j = 0; j < difference; j++) {
                        tempBinIndex += "0";
                    }
                    tempBinIndex += binIndex;
                    System.out.println("Index : " + tempBinIndex);
                } else {
                    System.out.println("Index : " + binIndex);
                }
                // converting finish -------------------------------------------------------
                System.out.println("Global Depth : " + globalDepth);
                System.out.println("Local Depth : " + searchElement.getLocalDepth());
                return true;
            }
            i++;
        }
        return false;
    }

    public long hashCode(V key) {
        long hash = 0;
        int g = 31;
        int m = 1000000007;
        int length = key.toString().length();
        for (int i = 0; i < length; i++) {
            hash = (g * hash + key.toString().charAt(i)) % m;
        }
        return hash;
    }

    private void splitBucket(LinkedList<Bucket<K, V>> currentLocation) {

        // clear index ,  which bucket causes to collision
        for (int index = 0; index < hashTable.length; index++) {
            if (hashTable[index] != null && hashTable[index].get(0) == currentLocation.get(0)) {
                hashTable[index] = null;
            }
        }

        // reload all collisions elements
        for (int i = 0; i < currentLocation.size(); i++) {
            for (int j = 0; j < currentLocation.get(i).getCount(); j++) {
                insert(currentLocation.get(i).getKey(), currentLocation.get(i).getWord());
            }
        }

    }

    @SuppressWarnings("unchecked")
    private void doubleSize() {
        // hold old values for insertion to new table
        LinkedList<Bucket<K, V>>[] tempHashTable = new LinkedList[hashTable.length];
        for (int i = 0; i < hashTable.length; i++) {
            tempHashTable[i] = hashTable[i];
        }

        enlargeCapacity();
        for (int i = 0; i < tempHashTable.length; i++) {
            if (tempHashTable[i] != null) {
                // start from first not null index and select it first node
                int localDepthOfNode = tempHashTable[i].getFirst().getLocalDepth();
                String binaryKeyAccordingToLocalDepth = tempHashTable[i].getFirst().getBinaryKey();
                // after select take it binary index according to local depth
                if (binaryKeyAccordingToLocalDepth.length() > localDepthOfNode) {
                    binaryKeyAccordingToLocalDepth = binaryKeyAccordingToLocalDepth.substring(
                            binaryKeyAccordingToLocalDepth.length() - localDepthOfNode,
                            binaryKeyAccordingToLocalDepth.length());
                }

                // after decide it index value put it to new table index which provide it's
                // index value
                for (int k = 0; k < hashTable.length; k++) {
                    long index = k;
                    String hashtableIndex = convertBinary(index);
                    // reduce indexing bit according to related local depth of selected bucket
                    if (hashtableIndex.length() > localDepthOfNode)
                        hashtableIndex = hashtableIndex.substring(hashtableIndex.length() - localDepthOfNode,
                                hashtableIndex.length());

                    // if they are equal locate node
                    if (convertDecimal(hashtableIndex) == convertDecimal(binaryKeyAccordingToLocalDepth)) {
                        hashTable[k] = tempHashTable[i];
                    }
                }

            }

        }
    }

    private String convertBinary(long index) {

        List<Integer> binary = new ArrayList<>();
        int id = 0;
        long num = (long) index;
        String binaryKey = "";

        // Number should be positive
        while (num > 0) {
            binary.add((int) num % 2);
            id++;
            num = num / 2;
        }

        for (int i = id - 1; i >= 0; i--)
            binaryKey += binary.get(i);

        if ((long) index == 0)
            return "0";

        return binaryKey;
    }

    private int locateElement(String binarykey, int bitAmount) {

        int sizeOfKey = binarykey.length();
        try {
            String indexingPart;
            if (bitAmount > sizeOfKey) {
                indexingPart = binarykey;
            } else {
                indexingPart = binarykey.substring(sizeOfKey - bitAmount, sizeOfKey);
            }
            return convertDecimal(indexingPart);

        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    private int convertDecimal(String key) {
        String num = key;
        int decimal_num = 0, base = 1;

        for (int i = num.length(); i > 0; i--) {
            decimal_num = decimal_num + Integer.parseInt(num.substring(i - 1, i)) * base;
            base = base * 2;
        }

        return decimal_num;
    }

    @SuppressWarnings("unchecked")
    private void enlargeCapacity() {
        int newGlobalDepth = globalDepth + 1;
        int newCapacity = (int) Math.pow(2, newGlobalDepth);

        LinkedList<Bucket<K, V>>[] tempHashTable = new LinkedList[newCapacity];
        hashTable = tempHashTable;
        capacity = newCapacity;
        globalDepth = newGlobalDepth;
    }

    private Bucket<K, V> isListElementExist(LinkedList<Bucket<K, V>> existList, K key) {
        int i = 0;
        while (existList.size() > i) {
            if (existList.get(i).getKey().toString().equalsIgnoreCase(key.toString())) {
                return existList.get(i);
            }
            i++;
        }
        return null;
    }

}