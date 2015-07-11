package delight.keyvalue.operations;

import delight.async.callbacks.ValueCallback;
import delight.keyvalue.Store;
import delight.keyvalue.StoreImplementation;

/**
 * A generic operation to be performed on a {@link Store}.
 * 
 * @author <a href="http://www.mxro.de">Max Rohde</a>
 *
 */
public interface StoreOperation<K, V> {

    public void applyOn(StoreImplementation<K, V> store, ValueCallback<Object> callback);

}
