package delight.keyvalue.internal.operations;

import delight.async.callbacks.ValueCallback;
import delight.functional.Function;
import delight.keyvalue.StoreImplementation;
import delight.keyvalue.operations.StoreOperation;

public class CountOperation<V> implements StoreOperation<String, V> {

    private String keyStartsWith;

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
    public void modifyKeysAfterGet(final Function<String, String> func) {

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
            callback.onSuccess(0);
            return;
        }
        store.count(keyStartsWith, new ValueCallback<Integer>() {

            @Override
            public void onFailure(final Throwable t) {
                callback.onFailure(t);
            }

            @Override
            public void onSuccess(final Integer value) {
                callback.onSuccess(value);
            }
        });
    }

    public CountOperation(final String keyStartsWith) {
        super();
        this.keyStartsWith = keyStartsWith;

    }

    @Override
    public String toString() {
        return "CountOperation [keyStartsWith=" + keyStartsWith + "]";
    }

}
