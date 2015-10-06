package delight.keyvalue.internal;

import delight.async.AsyncCommon;
import delight.async.callbacks.SimpleCallback;
import delight.async.callbacks.ValueCallback;
import delight.functional.Closure;
import delight.keyvalue.StoreEntry;
import delight.keyvalue.StoreImplementation;
import delight.keyvalue.internal.v01.StoreEntryData;
import delight.keyvalue.operations.StoreOperation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class HashMapAsyncMap<K, V> implements StoreImplementation<K, V> {

    private final HashMap<K, V> map;

    @Override
    public synchronized void put(final K key, final V value, final SimpleCallback callback) {
        map.put(key, value);
        callback.onSuccess();
    }

    @Override
    public synchronized void get(final K key, final ValueCallback<V> callback) {

        callback.onSuccess(map.get(key));
    }

    @Override
    public synchronized void remove(final K key, final SimpleCallback callback) {
        map.remove(key);
        callback.onSuccess();
    }

    @Override
    public synchronized V getSync(final K key) {

        return map.get(key);
    }

    @Override
    public synchronized void putSync(final K key, final V value) {
        map.put(key, value);
    }

    @Override
    public synchronized void removeSync(final K key) {
        map.remove(key);
    }

    @Override
    public synchronized void start(final SimpleCallback callback) {
        callback.onSuccess();
    }

    @Override
    public synchronized void stop(final SimpleCallback callback) {
        System.out.println("Stopping map. has values");
        System.out.println(map.keySet().toString().replaceAll(",", ",\n"));
        callback.onSuccess();
    }

    @Override
    public synchronized void commit(final SimpleCallback callback) {
        callback.onSuccess();
    }

    @Override
    public synchronized void performOperation(final StoreOperation<K, V> operation,
            final ValueCallback<Object> callback) {
        operation.applyOn(this, callback);
    }

    @Override
    public synchronized void clearCache() {
        // do nothing

    }

    @Override
    public synchronized void removeAll(final String keyStartsWith, final SimpleCallback callback) {

        System.out.println("XXXX removeAll " + keyStartsWith);

        for (final Entry<K, V> e : new HashMap<K, V>(this.map).entrySet()) {
            assert e.getKey() instanceof String;
            // System.out.println("compare: " + keyStartsWith + " with " +
            // e.getKey());
            if (e.getKey().toString().startsWith(keyStartsWith)) {
                System.out.println("XXXXX remove " + e.getKey());
                this.map.remove(e.getKey());
            }
        }

        callback.onSuccess();
    }

    @Override
    public synchronized void getAll(final String keyStartsWith, final int fromIdx, final int toIdx,
            final ValueCallback<List<StoreEntry<K, V>>> callback) {

        int found = 0;
        final int toFind = toIdx - fromIdx + 1;
        int idx = fromIdx;
        final List<Entry<K, V>> entrySet = new ArrayList<Entry<K, V>>(this.map.entrySet());

        final List<StoreEntry<K, V>> res = new ArrayList<StoreEntry<K, V>>(toFind);

        while (idx < entrySet.size() && (found <= toFind || toIdx == -1)) {
            final Entry<K, V> e = entrySet.get(idx);

            if (e.getKey().toString().startsWith(keyStartsWith)) {
                res.add(new StoreEntryData<K, V>(e.getKey(), e.getValue()));
                found++;
            }
            idx++;
        }

        callback.onSuccess(res);

    }

    @Override
    public synchronized void count(final String keyStartsWith, final ValueCallback<Integer> callback) {

        getAll(keyStartsWith, 0, -1, AsyncCommon.embed(callback, new Closure<List<StoreEntry<K, V>>>() {

            @Override
            public void apply(final List<StoreEntry<K, V>> matches) {
                callback.onSuccess(matches.size());
            }
        }));
    }

    @Override
    public synchronized void get(final List<K> keys, final ValueCallback<List<V>> callback) {
        final List<V> results = new ArrayList<V>(keys.size());

        for (final K key : keys) {
            results.add(getSync(key));
        }

        callback.onSuccess(results);

    }

    public HashMapAsyncMap() {
        super();
        this.map = new HashMap<K, V>();
    }

}
