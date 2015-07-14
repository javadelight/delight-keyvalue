package delight.keyvalue.jre.internal;

import delight.async.Operation;
import delight.async.callbacks.SimpleCallback;
import delight.async.callbacks.ValueCallback;
import delight.async.jre.Async;
import delight.keyvalue.Store;
import delight.keyvalue.operations.StoreOperation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * <p>
 * Bundles gets, even getSync ones.
 * 
 * @author <a href="http://www.mxro.de">Max Rohde</a>
 *
 */
public class MultiGetMap<K, V> implements Store<K, V> {

    private final Store<K, V> decorated;
    private final int delayInMs;

    private final ConcurrentLinkedQueue<Entry<K, ValueCallback<V>>> queue;

    private final void waitTillEmpty() {
        while (!queue.isEmpty()) {
            try {
                Thread.sleep(delayInMs);
            } catch (final InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private final void executeGetsAfterDelay() {
        try {
            Thread.sleep(delayInMs);
        } catch (final InterruptedException e) {
            throw new RuntimeException(e);
        }

        final List<Entry<K, ValueCallback<V>>> toProcess = new ArrayList<Entry<K, ValueCallback<V>>>();
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
        queue.offer(new EntryData<K, V>(key, callback));
        executeGetsAfterDelay();
    }

    @Override
    public V getSync(final K key) {
        return Async.waitFor(new Operation<V>() {

            @Override
            public void apply(final ValueCallback<V> callback) {
                queue.offer(new EntryData<K, V>(key, callback));
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
        decorated.stop(callback);
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

    public MultiGetMap(final int delayInMs, final Store<K, V> decorated) {
        super();
        this.decorated = decorated;
        this.delayInMs = delayInMs;
        this.queue = new ConcurrentLinkedQueue<Entry<K, ValueCallback<V>>>();
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

}
