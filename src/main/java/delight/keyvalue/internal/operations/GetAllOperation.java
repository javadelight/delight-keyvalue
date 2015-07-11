package delight.keyvalue.internal.operations;

import delight.async.AsyncCommon;
import delight.async.callbacks.ValueCallback;
import delight.functional.Closure;
import delight.functional.Function;
import delight.functional.Success;
import delight.keyvalue.StoreEntry;
import delight.keyvalue.StoreImplementation;
import delight.keyvalue.operations.StoreOperation;

public class GetAllOperation<V> implements StoreOperation<String, V> {

    private String keyStartsWith;
    private Closure<StoreEntry<String, V>> onEntry;

    boolean skip = false;

    @Override
    public void modifyKeys(final Function<String, String> func) {
        keyStartsWith = func.apply(keyStartsWith);
    }

    @Override
    public void ignoreKeys(final Function<String, Boolean> test) {
        if (test.apply(keyStartsWith)) {
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
    public void applyOn(final StoreImplementation<String, V> store, final ValueCallback<Object> callback) {
        if (skip) {
            callback.onSuccess(Success.INSTANCE);
            return;
        }
        store.getAll(keyStartsWith, onEntry, AsyncCommon.asSimpleCallbackAndReturnSuccess(callback));
    }

}
