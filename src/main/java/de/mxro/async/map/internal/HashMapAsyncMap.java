package de.mxro.async.map.internal;

import delight.async.callbacks.SimpleCallback;
import delight.async.callbacks.ValueCallback;

import java.util.HashMap;

import de.mxro.async.map.Store;
import de.mxro.async.map.operations.StoreOperation;

public class HashMapAsyncMap<K, V> implements Store<K, V> {

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
    public void performOperation(final StoreOperation operation) {

    }

    public HashMapAsyncMap() {
        super();
        this.map = new HashMap<K, V>();
    }

}
