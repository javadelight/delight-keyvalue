package de.mxro.async.map.internal.operations;

import delight.async.callbacks.ValueCallback;
import delight.functional.Success;

import de.mxro.async.map.StoreImplementation;
import de.mxro.async.map.operations.StoreOperation;

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

}
