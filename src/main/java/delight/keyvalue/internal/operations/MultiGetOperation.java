package delight.keyvalue.internal.operations;

import delight.async.AsyncCommon;
import delight.async.callbacks.ValueCallback;
import delight.functional.Closure;
import delight.functional.Function;
import delight.keyvalue.StoreImplementation;
import delight.keyvalue.operations.StoreOperation;

import java.util.ArrayList;
import java.util.List;

public class MultiGetOperation<K, V> implements StoreOperation<K, V> {

    private List<K> keys;
    private final List<Function<V, V>> valuesTrans;

    @Override
    public void modifyKeys(final Function<K, K> func) {
        final List<K> newKeys = new ArrayList<K>(keys.size());
        for (final K key : keys) {
            newKeys.add(key);
        }
        keys = newKeys;
    }

    @Override
    public void modifyKeysAfterGet(final Function<K, K> func) {

    }

    @Override
    public void ignoreKeys(final Function<K, Boolean> test) {
        throw new RuntimeException("Not supported!");
    }

    @Override
    public void modifyValuesBeforePut(final Function<V, V> func) {

    }

    @Override
    public void modifyValuesAfterGet(final Function<V, V> func) {
        valuesTrans.add(func);
    }

    @Override
    public void applyOn(final StoreImplementation<K, V> store, final ValueCallback<Object> callback) {
        store.get(keys, AsyncCommon.embed(callback, new Closure<List<V>>() {

            @Override
            public void apply(final List<V> o) {
                if (valuesTrans.size() == 0) {
                    callback.onSuccess(o);
                    return;
                }

                final List<V> modifiedValues = new ArrayList<V>(o.size());
                for (final V v : o) {

                    V value = v;

                    for (final Function<V, V> trans : valuesTrans) {
                        value = trans.apply(value);
                    }

                    modifiedValues.add(value);
                }

                callback.onSuccess(modifiedValues);

            }
        }));
    }

    public MultiGetOperation(final List<K> keys) {
        super();
        this.keys = keys;
        this.valuesTrans = new ArrayList<Function<V, V>>(0);
    }

}
