package delight.keyvalue.internal.operations;

import delight.async.AsyncCommon;
import delight.async.callbacks.ValueCallback;
import delight.functional.Function;
import delight.functional.Success;
import delight.keyvalue.StoreImplementation;
import delight.keyvalue.operations.StoreOperation;

public class RemoveAllOperation<V> implements StoreOperation<String, V> {

    String keyStartsWith;

    boolean skip = false;

    public String getKeyStartsWith() {
        return keyStartsWith;
    }

    @Override
    public void modifyKeys(final Function<String, String> func) {
        keyStartsWith = func.apply(keyStartsWith);

    }

    @Override
    public void modifyKeysAfterGet(final Function<String, String> func) {

    }

    @Override
    public void ignoreKeys(final Function<String, Boolean> test) {
        if (test.apply(keyStartsWith)) {
            skip = true;
        }
    }

    @Override
    public void modifyValuesBeforePut(final Function<V, V> func) {

    }

    @Override
    public void modifyValuesAfterGet(final Function<V, V> func) {

    }

    @Override
    public void applyOn(final StoreImplementation<String, V> store, final ValueCallback<Object> callback) {
        if (skip) {
            callback.onSuccess(Success.INSTANCE);
            return;
        }

        store.removeAll(keyStartsWith, AsyncCommon.asSimpleCallbackAndReturnSuccess(callback));
    }

    public RemoveAllOperation(final String keyStartsWith) {
        super();
        this.keyStartsWith = keyStartsWith;
    }

    @Override
    public String toString() {
        return super.toString() + ": keyStartsWith=" + keyStartsWith + "";
    }

}
