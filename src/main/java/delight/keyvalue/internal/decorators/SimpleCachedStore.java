package delight.keyvalue.internal.decorators;

import delight.async.AsyncCommon;
import delight.async.callbacks.SimpleCallback;
import delight.async.callbacks.ValueCallback;
import delight.functional.Closure;
import delight.functional.Fn;
import delight.functional.Success;
import delight.keyvalue.Store;
import delight.keyvalue.internal.operations.MultiGetOperation;
import delight.keyvalue.internal.operations.RemoveAllOperation;
import delight.keyvalue.operations.StoreOperation;
import delight.simplelog.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

final class SimpleCachedStore<K, V> implements Store<K, V> {

    private final static boolean ENABLE_LOG = false;

    private final Store<K, V> decorated;
    private final Map<K, Object> cache;

    private final static Object NULL = Fn.object();

    @Override
    public void put(final K key, final V value, final SimpleCallback callback) {
        synchronized (cache) {
            if (value != null) {
                this.cache.put(key, value);
            } else {
                this.cache.put(key, NULL);
            }
        }
        decorated.put(key, value, callback);
    }

    @Override
    public void putSync(final K key, final V value) {
        synchronized (cache) {
            if (value != null) {
                this.cache.put(key, value);
            } else {
                this.cache.put(key, NULL);
            }
        }

        decorated.putSync(key, value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void get(final K key, final ValueCallback<V> callback) {
        final Object fromCache;
        // System.out.println("check " + key);
        synchronized (cache) {
            fromCache = this.cache.get(key);
        }

        // System.out.println("in cache " + fromCache);
        if (fromCache != null) {
            if (fromCache == NULL) {
                callback.onSuccess(null);
                return;
            } else {
                callback.onSuccess((V) fromCache);
                return;
            }

        }

        decorated.get(key, AsyncCommon.embed(callback, new Closure<V>() {

            @Override
            public void apply(final V o) {
                synchronized (cache) {
                    if (o != null) {
                        cache.put(key, o);
                    } else {
                        cache.put(key, NULL);
                    }
                }
                callback.onSuccess(o);
            }
        }));
    }

    @SuppressWarnings("unchecked")
    @Override
    public V getSync(final K key) {
        final Object fromCache;
        synchronized (cache) {
            fromCache = this.cache.get(key);
        }
        if (fromCache != null) {
            if (fromCache == NULL) {
                return null;
            } else {
                return (V) fromCache;
            }

        }

        final V o = decorated.getSync(key);
        synchronized (cache) {
            if (o != null) {
                cache.put(key, o);
            } else {
                cache.put(key, NULL);
            }
        }
        return o;
    }

    @Override
    public void remove(final K key, final SimpleCallback callback) {
        synchronized (cache) {
            this.cache.remove(key);
        }
        this.decorated.remove(key, callback);
    }

    @Override
    public void removeSync(final K key) {
        synchronized (cache) {
            this.cache.remove(key);
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

    @SuppressWarnings("unchecked")
    @Override
    public void performOperation(final StoreOperation<K, V> operation, final ValueCallback<Object> callback) {

        if (ENABLE_LOG) {
            Log.println(this + ": performOperation " + operation + " for " + this.decorated);
        }

        if (operation instanceof MultiGetOperation) {
            final MultiGetOperation<K, V> multiGetOperation = (MultiGetOperation<K, V>) operation;

            final List<V> results = new ArrayList<V>(multiGetOperation.getKeys().size());

            for (final K key : multiGetOperation.getKeys()) {
                synchronized (cache) {
                    final Object res = cache.get(key);

                    if (res == NULL) {
                        results.add(null);
                    } else if (res == null) {
                        results.add(null);
                    } else {
                        results.add((V) res);
                    }

                }

            }

            if (results.size() == multiGetOperation.getKeys().size()) {
                // System.out.println("found multi get in cache!");
                multiGetOperation.pushOnCallback(results, callback);
                return;
            }
        }

        this.decorated.performOperation(operation, AsyncCommon.embed(callback, new Closure<Object>() {

            @SuppressWarnings("rawtypes")
            @Override
            public void apply(final Object o) {
                if (operation instanceof RemoveAllOperation) {
                    if (o instanceof Success) {

                        final RemoveAllOperation removeAllOperation = (RemoveAllOperation) operation;

                        final String keyStartsWith = removeAllOperation.getKeyStartsWith();

                        final List<String> keysToDelete = new ArrayList<String>();

                        synchronized (cache) {

                            for (final K k : cache.keySet()) {
                                final String key = (String) k;

                                if (key.startsWith(keyStartsWith)) {
                                    keysToDelete.add(key);
                                }

                            }

                            for (final String key : keysToDelete) {
                                final Object oldValue = cache.remove(key);

                                assert oldValue != null;
                            }
                        }
                    }
                }

                if (operation instanceof MultiGetOperation) {
                    final MultiGetOperation mget = (MultiGetOperation) operation;

                    final List<V> results = (List<V>) o;

                    assert mget.getKeys().size() == results.size();

                    for (int i = 0; i < results.size(); i++) {
                        synchronized (cache) {
                            cache.put((K) mget.getKeys().get(i), (Object) results.get(i));
                        }
                    }

                }

                // System.out.println(operation.getClass());

                callback.onSuccess(o);

            }
        }));
    }

    public SimpleCachedStore(final Map<K, Object> cache, final Store<K, V> decorated) {
        super();
        this.decorated = decorated;
        this.cache = cache;
    }

}
