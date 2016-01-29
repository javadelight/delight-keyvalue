package delight.keyvalue.jre.internal;

import delight.async.AsyncCommon;
import delight.async.Operation;
import delight.async.callbacks.SimpleCallback;
import delight.async.callbacks.ValueCallback;
import delight.async.jre.Async;
import delight.concurrency.Concurrency;
import delight.concurrency.jre.ConcurrencyJre;
import delight.concurrency.wrappers.SimpleAtomicInteger;
import delight.concurrency.wrappers.SimpleExecutor;
import delight.keyvalue.Store;
import delight.keyvalue.operations.StoreOperation;
import delight.keyvalue.operations.StoreOperations;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * <p>
 * Bundles gets, even getSync ones.
 * 
 * @author <a href="http://www.mxro.de">Max Rohde</a>
 *
 */
public final class MultiGetMap<K, V> implements Store<K, V> {

    private final boolean ENABLE_LOG = true;

    private final Store<K, V> decorated;
    private final int delayInMs;

    private final ConcurrentLinkedQueue<Entry<K, ValueCallback<V>>> scheduled;
    private final SimpleAtomicInteger processing;
    private final Concurrency conn;
    private final MultiGetMap<K, V>.ProcessGets processGets;
    private final SimpleExecutor executor;
    private final MultiGetMap<K, V>.ProcessGetsDelayed processGetsDelayed;

    private final void waitTillEmpty() {
        final long startedAt = new Date().getTime();

        while ((!scheduled.isEmpty() || processing.get() > 0) && new Date().getTime() - startedAt < 5000) {
            try {
                Thread.sleep(delayInMs);
            } catch (final InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        if (!scheduled.isEmpty() || processing.get() > 0) {
            waitTillEmpty();
            throw new RuntimeException("Multi get map could not be shut down correctly. Items still processing: "
                    + processing.get() + " Scheduled: " + scheduled.size());
        }

    }

    private final void executeGetsAfterDelay() {

        this.executor.execute(processGetsDelayed);

    }

    private final class ProcessGetsDelayed implements Runnable {

        @Override
        public void run() {
            try {
                Thread.sleep(delayInMs);
            } catch (final InterruptedException e) {
                throw new RuntimeException(e);
            }

            processGets.run();
        }

    }

    private final class ProcessGets implements Runnable {
        @Override
        public void run() {
            processing.incrementAndGet();
            final List<K> toProcessKeys = new ArrayList<K>(scheduled.size() + 5);
            final Map<K, List<ValueCallback<V>>> toProcessCbs = new HashMap<K, List<ValueCallback<V>>>(
                    toProcessKeys.size());

            Entry<K, ValueCallback<V>> e;
            while ((e = scheduled.poll()) != null) {

                if (toProcessCbs.get(e.getKey()) == null) {
                    toProcessKeys.add(e.getKey());
                    toProcessCbs.put(e.getKey(), new ArrayList<ValueCallback<V>>(1));
                }

                toProcessCbs.get(e.getKey()).add(e.getValue());

            }

            if (ENABLE_LOG) {
                if (toProcessKeys.size() > 1) {
                    System.out.println(this + ": Perform batch get for: " + toProcessKeys);
                }
            }

            if (toProcessKeys.size() == 1) {
                final List<ValueCallback<V>> cbs = toProcessCbs.get(toProcessKeys.get(0));

                decorated.get(toProcessKeys.get(0), new ValueCallback<V>() {

                    @Override
                    public void onFailure(final Throwable t) {
                        for (final ValueCallback<V> cb : cbs) {
                            cb.onFailure(t);
                        }
                    }

                    @Override
                    public void onSuccess(final V value) {
                        // TODO Auto-generated method stub

                    }
                });

                return;
            }

            decorated.performOperation(StoreOperations.<K, V> getAll(toProcessKeys), new ValueCallback<Object>() {

                @Override
                public void onFailure(final Throwable t) {
                    processing.decrementAndGet();
                    for (final Entry<K, List<ValueCallback<V>>> entry : toProcessCbs.entrySet()) {
                        for (final ValueCallback<V> cb : entry.getValue()) {

                            cb.onFailure(t);
                        }
                    }

                }

                @Override
                public void onSuccess(final Object value) {
                    processing.decrementAndGet();
                    final List<V> results = (List<V>) value;

                    assert results.size() == toProcessCbs.size();

                    for (int i = 0; i < results.size(); i++) {
                        for (final ValueCallback<V> cb : toProcessCbs.get(toProcessKeys.get(i))) {

                            cb.onSuccess(results.get(i));
                        }

                    }
                }

            });

        }
    }

    @Override
    public void put(final K key, final V value, final SimpleCallback callback) {
        waitTillEmpty();
        decorated.put(key, value, callback);
    }

    @Override
    public void putSync(final K key, final V value) {
        waitTillEmpty();
        decorated.putSync(key, value);
    }

    @Override
    public void get(final K key, final ValueCallback<V> callback) {
        scheduled.offer(new EntryData<K, V>(key, callback));
        executeGetsAfterDelay();
    }

    @Override
    public V getSync(final K key) {
        return Async.waitFor(new Operation<V>() {

            @Override
            public void apply(final ValueCallback<V> callback) {
                scheduled.offer(new EntryData<K, V>(key, callback));
                executeGetsAfterDelay();
            }
        });
    }

    @Override
    public void remove(final K key, final SimpleCallback callback) {
        waitTillEmpty();
        decorated.remove(key, callback);
    }

    @Override
    public void removeSync(final K key) {
        waitTillEmpty();
        decorated.removeSync(key);
    }

    @Override
    public void start(final SimpleCallback callback) {
        decorated.start(callback);
    }

    @Override
    public void stop(final SimpleCallback callback) {
        waitTillEmpty();
        executor.shutdown(AsyncCommon.embed(callback, new Runnable() {

            @Override
            public void run() {
                decorated.stop(callback);
            }
        }));

    }

    @Override
    public void commit(final SimpleCallback callback) {
        waitTillEmpty();
        decorated.commit(callback);
    }

    @Override
    public void performOperation(final StoreOperation<K, V> operation, final ValueCallback<Object> callback) {
        waitTillEmpty();
        decorated.performOperation(operation, callback);
    }

    public static class EntryData<K, V> implements Entry<K, ValueCallback<V>> {
        private final K key;
        private final ValueCallback<V> callback;

        public EntryData(final K key, final ValueCallback<V> callback) {
            this.key = key;
            this.callback = callback;
        }

        @Override
        public K getKey() {

            return key;
        }

        @Override
        public ValueCallback<V> getValue() {
            return callback;
        }

        @Override
        public ValueCallback<V> setValue(final ValueCallback<V> value) {
            throw new RuntimeException("not supported");
        }
    }

    public MultiGetMap(final int delayInMs, final Store<K, V> decorated) {
        super();
        this.decorated = decorated;
        this.delayInMs = delayInMs;
        this.scheduled = new ConcurrentLinkedQueue<Entry<K, ValueCallback<V>>>();
        this.conn = ConcurrencyJre.create();
        this.executor = this.conn.newExecutor().newParallelExecutor(1, this);
        this.processing = this.conn.newAtomicInteger(0);
        this.processGets = new ProcessGets();
        this.processGetsDelayed = new ProcessGetsDelayed();
    }

}
