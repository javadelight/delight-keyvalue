package delight.keyvalue.internal;

import delight.async.Value;
import delight.async.callbacks.SimpleCallback;
import delight.async.callbacks.ValueCallback;
import delight.functional.Closure;
import delight.keyvalue.StoreEntry;
import delight.keyvalue.StoreImplementation;
import delight.keyvalue.internal.v01.StoreEntryData;
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
    public void removeAll(final String keyStartsWith, final SimpleCallback callback) {
        for (final Entry<K, V> e : new HashMap<K, V>(this.map).entrySet()) {
            assert e.getKey() instanceof String;
            if (e.getKey().toString().startsWith(keyStartsWith)) {
                this.map.remove(e.getKey());
            }
        }

        callback.onSuccess();
    }

    @Override
    public void getAll(final String keyStartsWith, final Closure<StoreEntry<K, V>> onEntry,
            final SimpleCallback onCompleted) {
        for (final Entry<K, V> e : this.map.entrySet()) {
            assert e.getKey() instanceof String;
            if (e.getKey().toString().startsWith(keyStartsWith)) {
                onEntry.apply(new StoreEntryData<K, V>(e.getKey(), e.getValue()));
            }
        }

        onCompleted.onSuccess();

    }

    @Override
    public void count(final String keyStartsWith, final ValueCallback<Integer> callback) {
        final Value<Integer> count = new Value<Integer>(0);
        getAll(keyStartsWith, new Closure<StoreEntry<K,V>>() {

            @Override
            public void apply(final StoreEntry<K, V> o) {
                count.set(count.get()+1);
            }
        }, new SimpleCallback() {

            @Override
            public void onFailure(final Throwable t) {
                callback.onFailure(t);
            }

            @Override
            public void onSuccess() {
                c
            }
        });
    }

    public HashMapAsyncMap() {
        super();
        this.map = new HashMap<K, V>();
    }

}
