package delight.keyvalue.internal.decorators;

import delight.async.callbacks.SimpleCallback;
import delight.async.callbacks.ValueCallback;
import delight.functional.Function;
import delight.keyvalue.Store;
import delight.keyvalue.operations.StoreOperation;

/**
 * <P>
 * Will ignore keys (e.g. not store them in the map) if they don't satisfy a
 * specified filter.
 * 
 * @author <a href="http://www.mxro.de">Max Rohde</a>
 *
 */
class IgnoreKeysStore<K, V> implements Store<K, V> {

    private final Function<K, Boolean> filter;
    private final Store<K, V> decorated;

    @Override
    public void put(final K key, final V value, final SimpleCallback callback) {

        if (filter.apply(key)) {
            callback.onSuccess();
            return;
        }

        this.decorated.put(key, value, callback);
    }

    @Override
    public void get(final K key, final ValueCallback<V> callback) {
        if (filter.apply(key)) {
            callback.onSuccess(null);
            return;
        }
        this.decorated.get(key, callback);
    }

    @Override
    public void remove(final K key, final SimpleCallback callback) {
        if (filter.apply(key)) {
            callback.onSuccess();
            return;
        }
        this.decorated.remove(key, callback);
    }

    @Override
    public V getSync(final K key) {
        if (filter.apply(key)) {
            return null;
        }
        return this.decorated.getSync(key);
    }

    @Override
    public void putSync(final K key, final V value) {
        if (filter.apply(key)) {
            return;
        }
        this.decorated.putSync(key, value);
    }

    @Override
    public void removeSync(final K key) {
        if (filter.apply(key)) {
            return;
        }
        this.decorated.removeSync(key);
    }

    @Override
    public void start(final SimpleCallback callback) {
        this.decorated.start(callback);
    }

    @Override
    public void stop(final SimpleCallback callback) {
        this.decorated.stop(callback);
    }

    @Override
    public void commit(final SimpleCallback callback) {
        this.decorated.commit(callback);
    }

    @Override
    public void performOperation(final StoreOperation<K, V> operation, final ValueCallback<Object> callback) {
        operation.ignoreKeys(filter);
        this.decorated.performOperation(operation, callback);
    }

    public IgnoreKeysStore(final Function<K, Boolean> filter, final Store<K, V> decorated) {
        super();
        this.filter = filter;
        this.decorated = decorated;
    }

}
