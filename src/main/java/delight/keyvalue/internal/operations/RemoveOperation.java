package delight.keyvalue.internal.operations;

import delight.async.AsyncCommon;
import delight.async.callbacks.ValueCallback;
import delight.keyvalue.Store;
import delight.keyvalue.StoreImplementation;
import delight.keyvalue.operations.StoreOperation;

/**
 * An object representation of a remove operation on an {@link Store}.
 * 
 * @author <a href="http://www.mxro.de">Max Rohde</a>
 *
 */
public class RemoveOperation<K, V> implements StoreOperation<K, V> {

    private final K key;

    public K getKey() {
        return key;
    }

    public RemoveOperation(final K key) {
        super();
        this.key = key;

    }

    @Override
    public void applyOn(final StoreImplementation<K, V> store, final ValueCallback<Object> callback) {
        store.remove(key, AsyncCommon.asSimpleCallbackAndReturnSuccess(callback));
    }

}
