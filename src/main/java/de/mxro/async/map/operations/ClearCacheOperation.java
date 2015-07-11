package de.mxro.async.map.operations;

import delight.async.callbacks.ValueCallback;
import delight.functional.Success;

import de.mxro.async.map.StoreImplementation;

/**
 * If the persistence implementation supports a local cache, calling this method
 * clears this cache.
 * 
 * @author <a href="http://www.mxro.de">Max Rohde</a>
 *
 */
public class ClearCacheOperation<K, V> implements StoreOperation<K, V, Success> {

    @Override
    public void applyOn(final StoreImplementation<K, V> store, final ValueCallback<Success> callback) {
        store.clearCache();
    }

}
