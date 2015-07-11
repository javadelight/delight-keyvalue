package delight.keyvalue.internal.operations;

import delight.async.AsyncCommon;
import delight.async.callbacks.ValueCallback;
import delight.functional.Function;
import delight.functional.Success;
import delight.keyvalue.StoreImplementation;
import delight.keyvalue.operations.StoreOperation;

public class RemoveAllOperation<K, V> implements StoreOperation<K, V> {

    String keyStartsWith;

    boolean skip = false;

    @Override
    public void modifyKeys(final Function<K, K> func) {
        if (keyTest instanceof StartsWithTest) {
            final StartsWithTest startsWithTest = (StartsWithTest) keyTest;

            keyTest = (Function<K, Boolean>) new StartsWithTest((String) func.apply((K) startsWithTest.startsWith()));
            return;

        }

        final Function<K, Boolean> oldKeyTest = keyTest;
        keyTest = new Function<K, Boolean>() {

            @Override
            public Boolean apply(final K input) {
                return oldKeyTest.apply(func.apply(input));
            }

        };

    }

    @Override
    public void ignoreKeys(final Function<K, Boolean> test) {
        if (test.apply(commonKey)) {
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
    public void applyOn(final StoreImplementation<K, V> store, final ValueCallback<Object> callback) {
        if (skip) {
            callback.onSuccess(Success.INSTANCE);
            return;
        }

        store.removeAll(commonKey, AsyncCommon.asSimpleCallbackAndReturnSuccess(callback));
    }

}
