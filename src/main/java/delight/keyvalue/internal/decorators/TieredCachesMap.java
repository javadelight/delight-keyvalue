package delight.keyvalue.internal.decorators;

import delight.async.callbacks.SimpleCallback;
import delight.async.callbacks.ValueCallback;
import delight.keyvalue.Store;
import delight.keyvalue.operations.StoreOperation;

class TieredCachesMap<K, V> implements Store<K, V> {

    private final Store<K, V> secondaryCache;
    private final Store<K, V> primaryCache;

    @Override
    public void put(final K key, final V value, final SimpleCallback callback) {

        primaryCache.put(key, value, new SimpleCallback() {

            @Override
            public void onFailure(final Throwable t) {
                callback.onFailure(t);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess();

                secondaryCache.put(key, value, new SimpleCallback() {

                    @Override
                    public void onFailure(final Throwable t) {
                        throw new RuntimeException(t);
                    }

                    @Override
                    public void onSuccess() {

                    }
                });
            }
        });
    }

    @Override
    public void putSync(final K key, final V value) {

        primaryCache.putSync(key, value);

        secondaryCache.putSync(key, value);
    }

    @Override
    public void get(final K key, final ValueCallback<V> callback) {
        primaryCache.get(key, new ValueCallback<V>() {

            @Override
            public void onFailure(final Throwable t) {
                callback.onFailure(t);
            }

            @Override
            public void onSuccess(final V value) {
                if (value != null) {
                    callback.onSuccess(value);
                    return;
                }

                secondaryCache.get(key, new ValueCallback<V>() {

                    @Override
                    public void onFailure(final Throwable t) {
                        callback.onFailure(t);
                    }

                    @Override
                    public void onSuccess(final V value) {
                        callback.onSuccess(value);

                        if (value == null) {
                            return;
                        }

                        // placing value in cache
                        primaryCache.put(key, value, new SimpleCallback() {

                            @Override
                            public void onFailure(final Throwable t) {
                                throw new RuntimeException(t);
                            }

                            @Override
                            public void onSuccess() {

                            }
                        });

                    }
                });

            }
        });

    }

    @SuppressWarnings("unchecked")
    @Override
    public V getSync(final K key) {
        final Object fromCache = this.primaryCache.getSync(key);
        if (fromCache != null) {
            return (V) fromCache;

        }

        return secondaryCache.getSync(key);
    }

    @Override
    public void remove(final K key, final SimpleCallback callback) {

        this.primaryCache.remove(key, new SimpleCallback() {

            @Override
            public void onFailure(final Throwable t) {
                callback.onFailure(t);
            }

            @Override
            public void onSuccess() {
                secondaryCache.remove(key, callback);
            }
        });
    }

    @Override
    public void removeSync(final K key) {
        this.primaryCache.removeSync(key);
        this.secondaryCache.removeSync(key);
    }

    @Override
    public void start(final SimpleCallback callback) {
        this.secondaryCache.start(new SimpleCallback() {

            @Override
            public void onFailure(final Throwable t) {
                callback.onFailure(t);
            }

            @Override
            public void onSuccess() {
                primaryCache.start(callback);
            }
        });
    }

    @Override
    public void stop(final SimpleCallback callback) {
        this.secondaryCache.stop(new SimpleCallback() {

            @Override
            public void onFailure(final Throwable t) {
                callback.onFailure(t);
            }

            @Override
            public void onSuccess() {
                primaryCache.stop(callback);
            }
        });
    }

    @Override
    public void commit(final SimpleCallback callback) {
        this.secondaryCache.commit(new SimpleCallback() {

            @Override
            public void onFailure(final Throwable t) {
                callback.onFailure(t);
            }

            @Override
            public void onSuccess() {
                primaryCache.commit(callback);
            }
        });
    }

    @Override
    public void performOperation(final StoreOperation<Object, Object> operation, final ValueCallback<Object> callback) {
        this.secondaryCache.performOperation(operation, callback);
        this.primaryCache.performOperation(operation, callback);
    }

    public TieredCachesMap(final Store<K, V> cache, final Store<K, V> decorated) {
        super();
        this.secondaryCache = decorated;
        this.primaryCache = cache;
    }

}
