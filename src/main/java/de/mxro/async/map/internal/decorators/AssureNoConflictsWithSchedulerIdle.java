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
            decorated.put(key, value);
        }
    }

    @Override
    public void get(final K key, final ValueCallback<V> callback) {
        // TODO Auto-generated method stub

    }

    @Override
    public V getSync(final K key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void remove(final K key, final SimpleCallback callback) {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeSync(final K key) {
        // TODO Auto-generated method stub

    }

    @Override
    public void start(final SimpleCallback callback) {
        // TODO Auto-generated method stub

    }

    @Override
    public void stop(final SimpleCallback callback) {
        // TODO Auto-generated method stub

    }

    @Override
    public void commit(final SimpleCallback callback) {
        // TODO Auto-generated method stub

    }

    @Override
    public void performOperation(final MapOperation operation) {
        // TODO Auto-generated method stub

    }

}
