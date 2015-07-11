package delight.keyvalue.internal.operations;

import delight.async.callbacks.ValueCallback;
import delight.functional.Function;
import delight.functional.Success;
import delight.keyvalue.StoreImplementation;
import delight.keyvalue.operations.StoreOperation;

/**
 * If the persistence implementation supports a local cache, calling this method
 * clears this cache.
 * 
 * @author <a href="http://www.mxro.de">Max Rohde</a>
 *
 */
public class ClearCacheOperation<K, V> implements StoreOperation<K, V> {

    @Override
    public void applyOn(final StoreImplementation<K, V> store, final ValueCallback<Object> callback) {
        store.clearCache();
        callback.onSuccess(Success.INSTANCE);
    }

    @Override
    public void modifyKeys(final Function<K, K> func) {

    }

    @Override
    public void ignoreKeys(final Function<K, Boolean> test) {

    }

    @Override
    public void modifyValuesBeforePut(final Function<V, V> func) {

    }

    @Override
    public void modifyValuesAfterGet(final Function<V, V> func) {

    }

}
