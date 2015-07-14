package delight.keyvalue.jre.internal;

import delight.async.callbacks.SimpleCallback;
import delight.async.callbacks.ValueCallback;
import delight.keyvalue.Store;
import delight.keyvalue.operations.StoreOperation;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * <p>
 * Bundles gets, even getSync ones.
 * 
 * @author <a href="http://www.mxro.de">Max Rohde</a>
 *
 */
public class MultiGetMap<K, V> implements Store<K, V> {

    private final Store<K, V> decorated;
    private final int delayInMs;

    private final ConcurrentLinkedQueue<Entry<String, ValueCallback<V>>> queue;

    @Override
    public void put(final K key, final V value, final SimpleCallback callback) {
        decorated.put(key, value, callback);
    }

    @Override
    public void putSync(final K key, final V value) {
        decorated.putSync(key, value);
    }

    @Override
    public void get(final K key, final ValueCallback<V> callback) {
        decorated.get(key, callback);
    }

    @Override
    public V getSync(final K key) {

        return decorated.getSync(key);
    }

    @Override
    public void remove(final K key, final SimpleCallback callback) {
        decorated.remove(key, callback);
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
    public void performOperation(final StoreOperation<K, V> operation, final ValueCallback<Object> callback) {
        decorated.performOperation(operation, callback);
    }

    public MultiGetMap(final int delayInMs, final Store<K, V> decorated) {
        super();
        this.decorated = decorated;
        this.delayInMs = delayInMs;
    }

}
