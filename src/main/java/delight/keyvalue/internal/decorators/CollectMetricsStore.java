package delight.keyvalue.internal.decorators;

import delight.async.AsyncCommon;
import delight.async.callbacks.SimpleCallback;
import delight.async.callbacks.ValueCallback;
import delight.functional.Closure;
import delight.keyvalue.Store;
import delight.keyvalue.operations.StoreOperation;

import de.mxro.metrics.MetricsCommon;

final class CollectMetricsStore<K, V> implements Store<K, V> {

    private final Store<K, V> decorated;
    private final String metricName;

    @Override
    public void put(final K key, final V value, final SimpleCallback callback) {

        final long start = System.currentTimeMillis();

        decorated.put(key, value, new SimpleCallback() {

            @Override
            public void onFailure(final Throwable t) {

                callback.onFailure(t);
            }

            @Override
            public void onSuccess() {

                final long duration = System.currentTimeMillis() - start;
                // MetricsCommon.get().record(MetricsCommon.value(metricName +
                // "-put-stats", duration));
                MetricsCommon.get().record(MetricsCommon.increment(metricName + "-put-total", duration));
                MetricsCommon.get().record(MetricsCommon.increment(metricName + "-total", duration));

                callback.onSuccess();
            }
        });
    }

    @Override
    public void get(final K key, final ValueCallback<V> callback) {

        final long start = System.currentTimeMillis();

        decorated.get(key, new ValueCallback<V>() {

            @Override
            public void onFailure(final Throwable t) {
                callback.onFailure(t);
            }

            @Override
            public void onSuccess(final V value) {
                final long duration = System.currentTimeMillis() - start;
                // MetricsCommon.get().record(MetricsCommon.value(metricName +
                // "-put-stats", duration));
                MetricsCommon.get().record(MetricsCommon.increment(metricName + "-get-total", duration));
                MetricsCommon.get().record(MetricsCommon.increment(metricName + "-total", duration));
                callback.onSuccess(value);
            }
        });
    }

    @Override
    public void remove(final K key, final SimpleCallback callback) {
        final long start = System.currentTimeMillis();
        decorated.remove(key, new SimpleCallback() {

            @Override
            public void onFailure(final Throwable t) {
                callback.onFailure(t);
            }

            @Override
            public void onSuccess() {
                final long duration = System.currentTimeMillis() - start;
                // MetricsCommon.get().record(MetricsCommon.value(metricName +
                // "-put-stats", duration));
                MetricsCommon.get().record(MetricsCommon.increment(metricName + "-remove-total", duration));
                MetricsCommon.get().record(MetricsCommon.increment(metricName + "-total", duration));
                callback.onSuccess();
            }

        });

    }

    @Override
    public V getSync(final K key) {
        final long start = System.currentTimeMillis();

        final V res = decorated.getSync(key);

        final long duration = System.currentTimeMillis() - start;
        MetricsCommon.get().record(MetricsCommon.value(metricName + "-put-stats", duration));
        MetricsCommon.get().record(MetricsCommon.increment(metricName + "-getSync-total", duration));
        MetricsCommon.get().record(MetricsCommon.increment(metricName + "-total", duration));

        return res;
    }

    @Override
    public void putSync(final K key, final V value) {
        final long start = System.currentTimeMillis();
        decorated.putSync(key, value);
        final long duration = System.currentTimeMillis() - start;
        // MetricsCommon.get().record(MetricsCommon.value(metricName +
        // "-put-stats", duration));
        MetricsCommon.get().record(MetricsCommon.increment(metricName + "-putSync-total", duration));
        MetricsCommon.get().record(MetricsCommon.increment(metricName + "-total", duration));
    }

    @Override
    public void removeSync(final K key) {
        final long start = System.currentTimeMillis();
        decorated.removeSync(key);
        final long duration = System.currentTimeMillis() - start;
        // MetricsCommon.get().record(MetricsCommon.value(metricName +
        // "-put-stats", duration));
        MetricsCommon.get().record(MetricsCommon.increment(metricName + "-removeSync-total", duration));
        MetricsCommon.get().record(MetricsCommon.increment(metricName + "-total", duration));
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
        final long start = System.currentTimeMillis();

        this.decorated.performOperation(operation, AsyncCommon.embed(callback, new Closure<Object>() {
            @Override
            public void apply(final Object o) {

                final long duration = System.currentTimeMillis() - start;

                MetricsCommon.get().record(MetricsCommon.increment(metricName + "-operation-total", duration));
                MetricsCommon.get().record(MetricsCommon.increment(metricName + "-total", duration));

                callback.onSuccess(o);
            }
        }));
    }

    public CollectMetricsStore(final String metricName, final Store<K, V> decorated) {
        super();
        this.metricName = metricName;
        this.decorated = decorated;
    }

}
