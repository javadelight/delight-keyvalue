package delight.keyvalue.internal;

import delight.async.callbacks.SimpleCallback;
import delight.async.callbacks.ValueCallback;
import delight.keyvalue.StoreEntry;
import delight.keyvalue.StoreImplementation;
import delight.keyvalue.operations.StoreOperation;

import java.util.ArrayList;
import java.util.List;

public class NullStore<K, V> implements StoreImplementation<K, V> {

    @Override
    public void put(final K key, final V value, final SimpleCallback callback) {
        callback.onSuccess();
    }

    @Override
    public void putSync(final K key, final V value) {

    }

    @Override
    public void get(final K key, final ValueCallback<V> callback) {
        callback.onSuccess(null);
    }

    @Override
    public V getSync(final K key) {
        return null;
    }

    @Override
    public void remove(final K key, final SimpleCallback callback) {
        callback.onSuccess();
    }

    @Override
    public void removeSync(final K key) {

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
        callback.onSuccess(null);
    }

    @Override
    public void clearCache() {

    }

    @Override
    public void removeAll(final String keyStartsWith, final SimpleCallback callback) {
        callback.onSuccess();
    }

    @Override
    public void getAll(final String keyStartsWith, final int fromIdx, final int toIdx,
            final ValueCallback<List<StoreEntry<K, V>>> callback) {

        callback.onSuccess(new ArrayList<StoreEntry<K, V>>(0));
    }

    @Override
    public void count(final String keyStartsWith, final ValueCallback<Integer> callback) {
        callback.onSuccess(0);
    }

    @Override
    public void get(final List<K> keys, final ValueCallback<List<V>> callback) {
        final List<V> list = new ArrayList<V>(keys.size());

        for (int i = 0; i < keys.size(); i++) {
            list.add(null);
        }

        callback.onSuccess(list);
    }

}
