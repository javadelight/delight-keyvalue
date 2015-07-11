package delight.keyvalue.internal.v01;

import delight.keyvalue.StoreEntry;

import java.io.Serializable;

public class StoreEntryData<K, V> implements StoreEntry<K, V>, Serializable {

    private static final long serialVersionUID = 1L;
    public K key;
    public V value;

    @Override
    public K key() {
        return key;
    }

    @Override
    public V value() {
        return value;
    }

    /**
     * Only for deserialization.
     */
    public StoreEntryData() {
        super();
    }

    public StoreEntryData(final K key, final V value) {
        super();
        this.key = key;
        this.value = value;
    }

}
