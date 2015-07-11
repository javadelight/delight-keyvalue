package delight.keyvalue.internal;

import delight.async.callbacks.SimpleCallback;
import delight.async.callbacks.ValueCallback;
import delight.keyvalue.StoreEntry;
import delight.keyvalue.StoreImplementation;
import delight.keyvalue.operations.StoreOperation;

import java.util.HashMap;
import java.util.Map.Entry;

public class HashMapAsyncMap<K, V> implements StoreImplementation<K, V> {

    private final HashMap<K, V> map;

    @Override
    public void put(final K key, final V value, final SimpleCallback callback) {
        map.put(key, value);
        callback.onSuccess();
    }

    @Override
    public void get(final K key, final ValueCallback<V> callback) {

        callback.onSuccess(map.get(key));
    }

    @Override
    public void remove(final K key, final SimpleCallback callback) {
        map.remove(key);
        callback.onSuccess();
    }

    @Override
    public V getSync(final K key) {

        return map.get(key);
    }

    @Override
    public void putSync(final K key, final V value) {
        map.put(key, value);
    }

    @Override
    public void removeSync(final K key) {
        map.remove(key);
    }

    @Override
    public void start(final SimpleCallback callback) {
        callback.onSuccess();
    }

    @Override
    public void stop(final SimpleCallback callback) {
        callback.onSuccess();
    }

    @Override
    public void commit(final SimpleCallback callback) {
        callback.onSuccess();
    }

    @Override
    public void performOperation(final StoreOperation<K, V> operation, final ValueCallback<Object> callback) {
        operation.applyOn(this, callback);
    }

    @Override
    public void clearCache() {
        // do nothing

    }

    @Override
    public void removeAll(final K commonKeyElement, final SimpleCallback callback) {
        for (final Entry<K, V> e : this.map.entrySet()) {

        }
    }

    @Override
    public void getAll(final K commonKeyElement, final ValueCallback<StoreEntry<K, V>> entry,
            final SimpleCallback onCompleted) {
        // TODO Auto-generated method stub

    }

    public HashMapAsyncMap() {
        super();
        this.map = new HashMap<K, V>();
    }

}
