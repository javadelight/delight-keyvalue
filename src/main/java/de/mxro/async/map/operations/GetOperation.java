package de.mxro.async.map.operations;

import delight.async.callbacks.ValueCallback;

import de.mxro.async.map.StoreImplementation;

/**
 * An object representation of a Get operation on an asynchronous map.
 * 
 * @author <a href="http://www.mxro.de">Max Rohde</a>
 *
 * @param <V>
 * @see PureAsyncMap
 */
public class GetOperation<K, V> implements StoreOperation<K, V> {

    private final K key;

    public K getKey() {
        return key;
    }

    public GetOperation(final K key) {
        super();
        this.key = key;

    }

    @Override
    public void applyOn(final StoreImplementation<K, V> store, final ValueCallback<Object> callback) {
        store.get(key, (ValueCallback<V>) callback);
    }

}
