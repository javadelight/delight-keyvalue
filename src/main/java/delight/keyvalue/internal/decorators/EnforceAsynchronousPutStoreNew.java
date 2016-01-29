package delight.keyvalue.internal.decorators;

import delight.async.AsyncCommon;
import delight.async.AsyncFunction;
import delight.async.Operation;
import delight.async.callbacks.ListCallback;
import delight.async.callbacks.SimpleCallback;
import delight.async.callbacks.ValueCallback;
import delight.concurrency.Concurrency;
import delight.concurrency.schedule.SequentialOperationScheduler;
import delight.functional.Success;
import delight.keyvalue.Store;
import delight.keyvalue.internal.operations.MultiGetOperation;
import delight.keyvalue.internal.operations.RemoveAllOperation;
import delight.keyvalue.operations.StoreOperation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

final class EnforceAsynchronousPutStoreNew<K, V> implements Store<K, V> {

    private final Store<K, V> decorated;
    private final int delay;
    private final Concurrency concurrency;

    private final Map<K, Object> pendingValues;
    private final Map<K, Object> valuesWriting;

    private static final Object REMOVE = new Object() {

    };

    private final SequentialOperationScheduler putWorker;

    @Override
    public void put(final K key, final V value, final SimpleCallback callback) {
        synchronized (pendingValues) {
            pendingValues.put(key, value);
        }

        callback.onSuccess();

        scheduleWriteOperation();

    }

    @SuppressWarnings("unchecked")
    private V safeGet(final Object value) {
        if (value == REMOVE) {
            return null;
        }
        return (V) value;
    }

    private void scheduleWriteOperation() {
        if (putWorker.isRunning()) {
            return;
        }
        putWorker.schedule(new Operation<Success>() {

            @Override
            public void apply(final ValueCallback<Success> callback) {
                concurrency.newTimer().scheduleOnce(delay, new Runnable() {

                    @Override
                    public void run() {
                        writeValues(callback);
                    }
                });
            }

        }, new ValueCallback<Success>() {

            @Override
            public void onFailure(final Throwable t) {
                concurrency.newTimer().scheduleOnce(1, new Runnable() {

                    @Override
                    public void run() {
                        throw new RuntimeException(t);
                    }

                });
            }

            @Override
            public void onSuccess(final Success value) {

            }
        });
    }

    private final void writeValues(final ValueCallback<Success> callback) {

        assert valuesWriting.size() == 0;

        synchronized (valuesWriting) {
            synchronized (pendingValues) {
                valuesWriting.putAll(pendingValues);
                pendingValues.clear();
            }
        }

        final List<Operation<Success>> ops = new ArrayList<Operation<Success>>(valuesWriting.size());

        System.out.println("wirting values " + valuesWriting.size());

        for (final Entry<K, Object> e : valuesWriting.entrySet()) {
            ops.add(new Operation<Success>() {

                @SuppressWarnings("unchecked")
                @Override
                public void apply(final ValueCallback<Success> callback) {

                }

            });
        }

        AsyncCommon.map(valuesWriting.entrySet(), new AsyncFunction<Entry<K, Object>, Success>() {

            @Override
            public void apply(final Entry<K, Object> e, final ValueCallback<Success> callback) {
                if (e.getValue() == REMOVE) {
                    decorated.remove(e.getKey(), AsyncCommon.asSimpleCallback(callback));
                } else {
                    decorated.put(e.getKey(), (V) e.getValue(), AsyncCommon.asSimpleCallback(callback));
                }
            }
        }, new ListCallback<Success>() {

            @Override
            public void onSuccess(final List<Success> value) {
                valuesWriting.clear();
                callback.onSuccess(Success.INSTANCE);
            }

            @Override
            public void onFailure(final Throwable t) {
                valuesWriting.clear();
                callback.onFailure(t);

            }
        });

    }

    @Override
    public void putSync(final K key, final V value) {
        synchronized (pendingValues) {
            pendingValues.put(key, value);
        }

        scheduleWriteOperation();
    }

    @Override
    public void get(final K key, final ValueCallback<V> callback) {
        synchronized (pendingValues) {
            if (pendingValues.containsKey(key)) {
                callback.onSuccess(safeGet(pendingValues.get(key)));
                return;
            }
        }

        synchronized (valuesWriting) {
            if (valuesWriting.containsKey(key)) {
                callback.onSuccess(safeGet(valuesWriting.get(key)));
                return;
            }
        }

        decorated.get(key, callback);

    }

    @Override
    public V getSync(final K key) {
        synchronized (pendingValues) {
            if (pendingValues.containsKey(key)) {
                return safeGet(pendingValues.get(key));
            }
        }

        synchronized (valuesWriting) {
            if (valuesWriting.containsKey(key)) {
                return safeGet(valuesWriting.get(key));
            }
        }
        return decorated.getSync(key);
    }

    @Override
    public void remove(final K key, final SimpleCallback callback) {
        synchronized (pendingValues) {
            pendingValues.put(key, REMOVE);
        }

        scheduleWriteOperation();
    }

    @Override
    public void removeSync(final K key) {
        synchronized (pendingValues) {
            pendingValues.put(key, REMOVE);
        }

        scheduleWriteOperation();

    }

    @Override
    public void start(final SimpleCallback callback) {
        decorated.start(callback);
    }

    @Override
    public void stop(final SimpleCallback callback) {
        this.putWorker.shutdown(new ValueCallback<Success>() {

            @Override
            public void onFailure(final Throwable t) {
                callback.onFailure(t);
            }

            @Override
            public void onSuccess(final Success value) {
                callback.onSuccess();
            }
        });
    }

    @Override
    public void commit(final SimpleCallback callback) {
        decorated.commit(callback);
    }

    @Override
    public void performOperation(final StoreOperation<K, V> operation, final ValueCallback<Object> callback) {
        if (operation instanceof RemoveAllOperation) {
            @SuppressWarnings("unchecked")
            final RemoveAllOperation<V> removeAllOperation = (RemoveAllOperation<V>) operation;

            final String keyStartsWith = removeAllOperation.getKeyStartsWith();

            synchronized (pendingValues) {
                for (final Entry<K, Object> e : pendingValues.entrySet()) {
                    if (((String) e.getKey()).startsWith(keyStartsWith)) {
                        pendingValues.put(e.getKey(), REMOVE);
                    }
                }
            }
            callback.onSuccess(Success.INSTANCE);
            return;

        }

        if (operation instanceof MultiGetOperation) {
            final MultiGetOperation<K, V> multiGetOperation = (MultiGetOperation<K, V>) operation;

            final List<V> results = new ArrayList<V>(multiGetOperation.getKeys().size());
            for (final K key : multiGetOperation.getKeys()) {
                synchronized (pendingValues) {
                    if (pendingValues.containsKey(key)) {
                        results.add(safeGet(pendingValues.get(key)));
                    }
                }

                synchronized (valuesWriting) {
                    if (valuesWriting.containsKey(key)) {
                        results.add(safeGet(valuesWriting.get(key)));
                    }
                }

                if (results.size() == multiGetOperation.getKeys().size()) {
                    multiGetOperation.pushOnCallback(results, callback);
                    return;
                }

            }

        }

        // TODO can getAll be added here?

        // System.out.println("Waiting for operation: " + operation);

        putWorker.schedule(new Operation<Object>() {

            @Override
            public void apply(final ValueCallback<Object> callback) {
                decorated.performOperation(operation, callback);
            }

        }, callback);

    }

    public EnforceAsynchronousPutStoreNew(final int delay, final Concurrency concurrency, final Store<K, V> decorated) {
        super();
        this.decorated = decorated;
        this.delay = delay;
        this.concurrency = concurrency;
        this.pendingValues = new HashMap<K, Object>();
        this.valuesWriting = new HashMap<K, Object>();
        this.putWorker = new SequentialOperationScheduler(concurrency);
        this.putWorker.setEnforceOwnThread(true);
    }

}
