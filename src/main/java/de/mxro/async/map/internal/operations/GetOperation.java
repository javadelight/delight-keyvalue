package de.mxro.async.map.internal.operations;

import delight.async.AsyncCommon;
import delight.async.callbacks.ValueCallback;
import delight.functional.Closure;

import de.mxro.async.map.StoreImplementation;
import de.mxro.async.map.operations.StoreOperation;

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
        store.get(key, AsyncCommon.embed(callback, new Closure<V>() {

            @Override
            public void apply(final V o) {
                callback.onSuccess(o);
            }

        }));
    }

}
