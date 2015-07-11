package de.mxro.async.map.operations;

import delight.async.callbacks.ValueCallback;

import de.mxro.async.map.Store;

/**
 * A generic operation to be performed on a {@link Store}.
 * 
 * @author <a href="http://www.mxro.de">Max Rohde</a>
 *
 */
public interface StoreOperation<K, V, R> {

    public void applyOn(Store<K, V> store, ValueCallback<R> callback);

}
