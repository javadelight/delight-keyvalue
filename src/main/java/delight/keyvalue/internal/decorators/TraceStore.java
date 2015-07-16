package delight.keyvalue.internal.decorators;

import delight.async.callbacks.SimpleCallback;
import delight.async.callbacks.ValueCallback;
import delight.functional.Closure;
import delight.keyvalue.Store;
import delight.keyvalue.operations.StoreOperation;

final class TraceStore<K, V> implements Store<K, V> {

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
        messageReceiver.apply("BEFORE: remove " + key);
        decorated.remove(key, callback);
    }

    @Override
    public V getSync(final K key) {
        messageReceiver.apply("BEFORE: getSync " + key);
        final V res = decorated.getSync(key);
        messageReceiver.apply("AFTER: getSync got:" + res);
        return res;
    }

    @Override
    public void putSync(final K key, final V value) {
        messageReceiver.apply("BEFORE: putSync " + key);
        decorated.putSync(key, value);
    }

    @Override
    public void removeSync(final K key) {
        messageReceiver.apply("BEFORE: removeSync " + key);
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
    public void performOperation(final StoreOperation<K, V> operation, final ValueCallback<Object> callback) {
        this.decorated.performOperation(operation, callback);
    }

    public TraceStore(final Closure<String> messageReceiver, final Store<K, V> decorated) {
        super();
        this.messageReceiver = messageReceiver;
        this.decorated = decorated;
    }

}
