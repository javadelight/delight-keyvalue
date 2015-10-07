package delight.keyvalue.operations;

import delight.async.callbacks.ValueCallback;
import delight.functional.Function;
import delight.keyvalue.Store;
import delight.keyvalue.StoreImplementation;

/**
 * A generic operation to be performed on a {@link Store}.
 * 
 * @author <a href="http://www.mxro.de">Max Rohde</a>
 *
 */
public interface StoreOperation<K, V> {

    public void modifyKeys(Function<K, K> func);

    /**
     * <p>
     * To convert raw keys into filtered keys.
     * <p>
     * Require for getAll operation.
     * 
     * @param func
     */
    public void modifyKeysAfterGet(Function<K, K> func);

    public void ignoreKeys(Function<K, Boolean> test);

    public void modifyValuesBeforePut(Function<V, V> func);

    public void modifyValuesAfterGet(Function<V, V> func);

    public void applyOn(StoreImplementation<K, V> store, ValueCallback<Object> callback);

}
