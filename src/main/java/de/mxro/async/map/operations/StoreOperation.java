package de.mxro.async.map.operations;

import delight.async.callbacks.ValueCallback;

import de.mxro.async.map.Store;
import de.mxro.async.map.StoreImplementation;

/**
 * A generic operation to be performed on a {@link Store}.
 * 
 * @author <a href="http://www.mxro.de">Max Rohde</a>
 *
 */
public interface StoreOperation<K, V, R> {

    public void applyOn(StoreImplementation<K, V> store, ValueCallback<R> callback);

}
