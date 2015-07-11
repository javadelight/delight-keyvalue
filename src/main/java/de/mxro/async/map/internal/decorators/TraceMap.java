package de.mxro.async.map.internal.decorators;

import delight.async.callbacks.SimpleCallback;
import delight.async.callbacks.ValueCallback;
import delight.functional.Closure;

import de.mxro.async.map.Store;
import de.mxro.async.map.operations.StoreOperation;

final class TraceMap<K, V> implements Store<K, V> {

    private final Closure<String> messageReceiver;
    private final Store<K, V> decorated;

    @Override
    public void put(final K key, final V value, final SimpleCallback callback) {
        messageReceiver.apply("BEFORE: put " + key + " " + value);
        decorated.put(key, value, new SimpleCallback() {

            @Override
            public void onFailure(final Throwable t) {
                messageReceiver.apply("AFTER: put " + key + " " + value + " EXCEPTION: " + t);
                callback.onFailure(t);
            }

            @Override
            public void onSuccess() {
                messageReceiver.apply("AFTER: put " + key + " " + value + " SUCCESS");
                callback.onSuccess();
            }
        });
    }

    @Override
    public void get(final K key, final ValueCallback<V> callback) {
        messageReceiver.apply("BEFORE: get " + key);
        decorated.get(key, new ValueCallback<V>() {

            @Override
            public void onFailure(final Throwable t) {
                messageReceiver.apply("AFTER: get " + key + " EXCEPTION " + t);
                callback.onFailure(t);
            }

            @Override
            public void onSuccess(final V value) {
                messageReceiver.apply("AFTER: get " + key + " got " + value);
                callback.onSuccess(value);
            }
        });
    }

    @Override
    public void remove(final K key, final SimpleCallback callback) {
        decorated.remove(key, callback);
    }

    @Override
    public V getSync(final K key) {

        return decorated.getSync(key);
    }

    @Override
    public void putSync(final K key, final V value) {

        decorated.putSync(key, value);
    }

    @Override
    public void removeSync(final K key) {
        decorated.removeSync(key);
    }

    @Override
    public void start(final SimpleCallback callback) {
        decorated.start(callback);
    }

    @Override
    public void stop(final SimpleCallback callback) {
        decorated.stop(callback);
    }

    @Override
    public void commit(final SimpleCallback callback) {
        decorated.commit(callback);
    }

    @Override
    public void performOperation(final StoreOperation operation) {
        decorated.performOperation(operation);
    }

    public TraceMap(final Closure<String> messageReceiver, final Store<K, V> decorated) {
        super();
        this.messageReceiver = messageReceiver;
        this.decorated = decorated;
    }

}
