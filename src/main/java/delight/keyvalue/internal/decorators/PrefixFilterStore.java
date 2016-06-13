package delight.keyvalue.internal.decorators;

import delight.async.callbacks.SimpleCallback;
import delight.async.callbacks.ValueCallback;
import delight.functional.Function;
import delight.keyvalue.Store;
import delight.keyvalue.Stores;
import delight.keyvalue.operations.StoreOperation;
import delight.keyvalue.utils.InversePrefixFilter;
import delight.keyvalue.utils.NoFilter;

public class PrefixFilterStore<V> implements Store<String, V> {

    private final String prefix;
    private final Store<String, V> decorated;

    private final Store<String, V> filterStore;
    private final Function<String, String> inverseFilter;

    @Override
    public void put(final String key, final V value, final SimpleCallback callback) {
        this.filterStore.put(key, value, callback);
    }

    @Override
    public void putSync(final String key, final V value) {
        this.filterStore.putSync(key, value);
    }

    @Override
    public void get(final String key, final ValueCallback<V> callback) {
        this.filterStore.get(key, callback);
    }

    @Override
    public V getSync(final String key) {

        return this.filterStore.getSync(key);
    }

    @Override
    public void remove(final String key, final SimpleCallback callback) {
        this.filterStore.remove(key, callback);
    }

    @Override
    public void removeSync(final String key) {
        this.filterStore.removeSync(key);
    }

    @Override
    public void start(final SimpleCallback callback) {
        this.filterStore.start(callback);
    }

    @Override
    public void stop(final SimpleCallback callback) {
        this.filterStore.stop(callback);
    }

    @Override
    public void commit(final SimpleCallback callback) {
        this.filterStore.commit(callback);
    }

    @Override
    public void performOperation(final StoreOperation<String, V> operation, final ValueCallback<Object> callback) {
        // operation.modifyKeys(func);
        operation.modifyKeysAfterGet(this.inverseFilter);
        this.filterStore.performOperation(operation, callback);

    }

    public PrefixFilterStore(final String prefix, final Store<String, V> decorated) {
        super();
        this.prefix = prefix;
        this.decorated = decorated;

        final Function<String, String> filter = new Filter(prefix);

        final int prefixLength = prefix.length();

        this.inverseFilter = new InversePrefixFilter(prefixLength);

        this.filterStore = Stores.<String, V> filterKeys(filter, new NoFilter(), decorated);

    }

    private static final class Filter implements Function<String, String> {
        private final String prefix;

        private Filter(final String prefix) {
            this.prefix = prefix;
        }

        @Override
        public String apply(final String input) {

            return prefix + input;
        }
    }

}
