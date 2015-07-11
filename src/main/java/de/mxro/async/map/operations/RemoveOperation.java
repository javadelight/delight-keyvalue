package de.mxro.async.map.operations;

import delight.async.callbacks.ValueCallback;
import delight.functional.Success;

import de.mxro.async.map.Store;
import de.mxro.async.map.StoreImplementation;

/**
 * An object representation of a remove operation on an {@link Store}.
 * 
 * @author <a href="http://www.mxro.de">Max Rohde</a>
 *
 */
public class RemoveOperation<K, V> implements StoreOperation<K, V, Success> {

    private final K key;

    public K getKey() {
        return key;
    }

    public RemoveOperation(final K key) {
        super();
        this.key = key;

    }

    @Override
    public void applyOn(final StoreImplementation<K, V> store, final ValueCallback<Success> callback) {
        store.remove(key, callback);
    }

}
