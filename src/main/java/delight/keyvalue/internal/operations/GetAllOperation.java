package delight.keyvalue.internal.operations;

import delight.async.AsyncCommon;
import delight.async.callbacks.ValueCallback;
import delight.functional.Closure;
import delight.functional.Function;
import delight.functional.Success;
import delight.keyvalue.StoreEntry;
import delight.keyvalue.StoreImplementation;
import delight.keyvalue.internal.v01.StoreEntryData;
import delight.keyvalue.operations.StoreOperation;

import java.util.LinkedList;
import java.util.List;

public class GetAllOperation<V> implements StoreOperation<String, V> {

    private String keyStartsWith;

    private final List<Function<V, V>> afterGet;

    boolean skip = false;

    private final int fromIdx;

    private final int toIdx;

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
        store.getAll(keyStartsWith, fromIdx, toIdx, AsyncCommon.embed(callback, new Closure<List<V>>() {

            @Override
            public void apply(final List<V> res) {
               
                for (final o : res) {
                V value = o.value();
                for (final Function<V, V> f : afterGet) {
                    value = f.apply(value);
                }
                }
                
                
            }
        }));
                
                
                new Closure<StoreEntry<String, V>>() {

            @Override
            public void apply(final StoreEntry<String, V> o) {
                

                onEntry.apply(new StoreEntryData<String, V>(o.key(), value));

            }
        }, AsyncCommon.asSimpleCallbackAndReturnSuccess(callback));
    }

    public GetAllOperation(final String keyStartsWith, final int fromIdx, final int toIdx) {
        super();
        this.keyStartsWith = keyStartsWith;
        this.afterGet = new LinkedList<Function<V, V>>();
        this.fromIdx = fromIdx;
        this.toIdx = toIdx;
    }

}
