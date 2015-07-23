package delight.keyvalue.internal.decorators;

import delight.async.callbacks.SimpleCallback;
import delight.async.callbacks.ValueCallback;
import delight.functional.Fn;
import delight.keyvalue.Store;
import delight.keyvalue.internal.operations.RemoveAllOperation;
import delight.keyvalue.operations.StoreOperation;

import java.util.Map;

class SimpleCachedStore<K, V> implements Store<K, V> {

    private final Store<K, V> decorated;
    private final Map<K, Object> cache;

    private final static Object NULL = Fn.object();

    @Override
    public void put(final K key, final V value, final SimpleCallback callback) {
        if (value != null) {
            this.cache.put(key, value);
        } else {
            this.cache.put(key, NULL);
        }
        decorated.put(key, value, callback);
    }

    @Override
    public void putSync(final K key, final V value) {
        if (value != null) {
            this.cache.put(key, value);
        } else {
            this.cache.put(key, NULL);
        }

        decorated.putSync(key, value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void get(final K key, final ValueCallback<V> callback) {
        final Object fromCache = this.cache.get(key);
        if (fromCache != null) {
            if (fromCache == NULL) {
                callback.onSuccess(null);
                return;
            } else {
                callback.onSuccess((V) fromCache);
                return;
            }

        }

        decorated.get(key, callback);
    }

    @SuppressWarnings("unchecked")
    @Override
    public V getSync(final K key) {
        final Object fromCache = this.cache.get(key);
        if (fromCache != null) {
            if (fromCache == NULL) {
                return null;
            } else {
                return (V) fromCache;
            }

        }

        return decorated.getSync(key);
    }

    @Override
    public void remove(final K key, final SimpleCallback callback) {
        this.cache.remove(key);
        this.decorated.remove(key, callback);
    }

    @Override
    public void removeSync(final K key) {
        this.cache.remove(key);
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
        if (operation instanceof RemoveAllOperation) {
            final RemoveAllOperation removeAllOperation = (RemoveAllOperation) operation;

            final String keyStartsWith = removeAllOperation.getKeyStartsWith();

            for (final K k : this.cache.keySet()) {
                final String key = (String) k;
            }

        }
        this.decorated.performOperation(operation, callback);
    }

    public SimpleCachedStore(final Map<K, Object> cache, final Store<K, V> decorated) {
        super();
        this.decorated = decorated;
        this.cache = cache;
    }

}
