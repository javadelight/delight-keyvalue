package de.mxro.async.map.internal.decorators;

import delight.async.AsyncCommon;
import delight.async.Operation;
import delight.async.callbacks.SimpleCallback;
import delight.async.callbacks.ValueCallback;
import delight.concurrency.schedule.SequentialOperationScheduler;

import de.mxro.async.map.Store;
import de.mxro.async.map.operations.MapOperation;

public class OnlyExecuteOperationsWhenSchedulerIdle<K, V> implements Store<K, V> {

    SequentialOperationScheduler scheduler;
    Store<K, V> decorated;

    @Override
    public void put(final K key, final V value, final SimpleCallback callback) {
        if (scheduler.isRunning()) {
            scheduler.schedule(new Operation<Object>() {

                @Override
                public void apply(final ValueCallback<Object> callback) {
                    decorated.put(key, value, AsyncCommon.);
                }

            }, AsyncCommon.wrap(callback));
            return;
        }
    }

    @Override
    public void putSync(final K key, final V value) {
        // TODO Auto-generated method stub

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
