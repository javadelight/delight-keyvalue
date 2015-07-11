package delight.keyvalue.internal.operations;

import delight.async.AsyncCommon;
import delight.async.callbacks.ValueCallback;
import delight.functional.Function;
import delight.functional.Success;
import delight.keyvalue.StoreImplementation;
import delight.keyvalue.operations.StoreOperation;

public class RemoveAllOperation<K, V> implements StoreOperation<K, V> {

    K commonKey;

    boolean skip = false;

    @Override
    public void modifyKeys(final Function<K, K> func) {
        commonKey = func.apply(commonKey);
    }

    @Override
    public void ignoreKeys(final Function<K, Boolean> test) {
        if (test.apply(commonKey)) {
            skip = true;
        }
    }

    @Override
    public void modifyValuesBeforePut(final Function<V, V> func) {
        // TODO Auto-generated method stub

    }

    @Override
    public void modifyValuesAfterGet(final Function<V, V> func) {
        // TODO Auto-generated method stub

    }

    @Override
    public void applyOn(final StoreImplementation<K, V> store, final ValueCallback<Object> callback) {
        if (skip) {
            callback.onSuccess(Success.INSTANCE);
            return;
        }

        store.removeAll(commonKey, AsyncCommon.asSimpleCallbackAndReturnSuccess(callback));
    }

}
