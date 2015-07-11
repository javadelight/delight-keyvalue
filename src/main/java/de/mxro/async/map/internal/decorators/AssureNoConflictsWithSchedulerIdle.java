package de.mxro.async.map.internal.decorators;

import delight.async.AsyncCommon;
import delight.async.Operation;
import delight.async.callbacks.SimpleCallback;
import delight.async.callbacks.ValueCallback;
import delight.concurrency.schedule.SequentialOperationScheduler;

import de.mxro.async.map.Store;
import de.mxro.async.map.operations.MapOperation;

public class AssureNoConflictsWithSchedulerIdle<K, V> implements Store<K, V> {

    SequentialOperationScheduler scheduler;
    Store<K, V> decorated;

    private final SimpleCallback resumeScheduler(final SimpleCallback callback) {
        return new SimpleCallback() {

            @Override
            public void onFailure(final Throwable t) {
                scheduler.resume();
                callback.onFailure(t);
            }

            @Override
            public void onSuccess() {
                scheduler.resume();
                callback.onSuccess();
            }
        };
    }

    private final ValueCallback<V> resumeScheduler(final ValueCallback<V> callback) {
        return new ValueCallback<V>() {

            @Override
            public void onFailure(final Throwable t) {
                scheduler.resume();
                callback.onFailure(t);
            }

            @Override
            public void onSuccess(final V value) {
                scheduler.resume();
                callback.onSuccess(value);

            }
        };
    }

    @Override
    public void put(final K key, final V value, final SimpleCallback callback) {
        if (!scheduler.suspendIfNotRunning()) {
            scheduler.schedule(new Operation<Object>() {

                @Override
                public void apply(final ValueCallback<Object> callback) {
                    decorated.put(key, value, AsyncCommon.asSimpleCallbackAndReturnSuccess(callback));
                }

            }, AsyncCommon.asValueCallback(callback));
            return;
        }

        decorated.put(key, value, resumeScheduler(callback));
    }

    @Override
    public void putSync(final K key, final V value) {
        if (!scheduler.isRunning()) {
            decorated.putSync(key, value);
            return;
        }

        scheduler.schedule(new Operation<Object>() {

            @Override
            public void apply(final ValueCallback<Object> callback) {
                decorated.putSync(key, value);
            }

        }, AsyncCommon.asValueCallback(AsyncCommon.doNothing()));
    }

    @Override
    public void get(final K key, final ValueCallback<V> callback) {
        if (!scheduler.suspendIfNotRunning()) {
            scheduler.schedule(new Operation<V>() {

                @Override
                public void apply(final ValueCallback<V> callback) {
                    decorated.get(key, callback);
                }

            }, callback);
            return;
        }

        decorated.get(key, resumeScheduler(callback));
    }

    @Override
    public V getSync(final K key) {

        return decorated.getSync(key);
    }

    @Override
    public void remove(final K key, final SimpleCallback callback) {
        if (!scheduler.suspendIfNotRunning()) {
            scheduler.schedule(new Operation<Object>() {

                @Override
                public void apply(final ValueCallback<Object> callback) {
                    decorated.remove(key, AsyncCommon.asSimpleCallbackAndReturnSuccess(callback));
                }

            }, AsyncCommon.asValueCallback(callback));
            return;
        }

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
        if (!scheduler.suspendIfNotRunning()) {
            scheduler.schedule(new Operation<Object>() {

                @Override
                public void apply(final ValueCallback<Object> callback) {
                    decorated.commit(AsyncCommon.asSimpleCallbackAndReturnSuccess(callback));
                }

            }, AsyncCommon.asValueCallback(callback));
            return;
        }

        decorated.commit(callback);

    }

    @Override
    public void performOperation(final MapOperation operation) {
        if (!scheduler.suspendIfNotRunning()) {
            scheduler.schedule(new Operation<Object>() {

                @Override
                public void apply(final ValueCallback<Object> callback) {
                    decorated.performOperation(operation);
                }

            }, AsyncCommon.asValueCallback(AsyncCommon.doNothing()));
            return;
        }

        decorated.performOperation(operation);
    }

}
