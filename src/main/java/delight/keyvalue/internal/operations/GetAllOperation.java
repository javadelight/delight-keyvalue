package delight.keyvalue.internal.operations;

import delight.async.AsyncCommon;
import delight.async.callbacks.ValueCallback;
import delight.functional.Closure;
import delight.functional.Function;
import delight.functional.Success;
import delight.keyvalue.StoreEntry;
import delight.keyvalue.StoreImplementation;
import delight.keyvalue.operations.StoreOperation;

import java.util.LinkedList;
import java.util.List;

public class GetAllOperation<V> implements StoreOperation<String, V> {

    private String keyStartsWith;
    private final Closure<StoreEntry<String, V>> onEntry;

    private final List<Function<V, V>> afterGet;

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

    }

    @Override
    public void modifyValuesAfterGet(final Function<V, V> func) {
        this.afterGet.add(func);
    }

    @Override
    public void applyOn(final StoreImplementation<String, V> store, final ValueCallback<Object> callback) {
        if (skip) {
            callback.onSuccess(Success.INSTANCE);
            return;
        }
        store.getAll(keyStartsWith, new Closure<StoreEntry<String, V>>() {

            @Override
            public void apply(final StoreEntry<String, V> o) {
                // TODO Auto-generated method stub

            }
        }, AsyncCommon.asSimpleCallbackAndReturnSuccess(callback));
    }

    public GetAllOperation(final String keyStartsWith, final Closure<StoreEntry<String, V>> onEntry) {
        super();
        this.keyStartsWith = keyStartsWith;
        this.onEntry = onEntry;
        this.afterGet = new LinkedList<Function<V, V>>();
    }

}
