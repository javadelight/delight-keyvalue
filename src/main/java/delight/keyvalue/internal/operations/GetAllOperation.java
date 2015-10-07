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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GetAllOperation<V> implements StoreOperation<String, V> {

    private String keyStartsWith;

    private final List<Function<V, V>> afterGetValues;
    private final List<Function<String, String>> afterGetKeys;

    boolean skip = false;

    private final int fromIdx;

    private final int toIdx;

    @Override
    public void modifyKeys(final Function<String, String> func) {
        keyStartsWith = func.apply(keyStartsWith);
    }

    @Override
    public void modifyKeysAfterGet(final Function<String, String> func) {
        afterGetKeys.add(func);
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
        this.afterGetValues.add(func);
    }

    @Override
    public void applyOn(final StoreImplementation<String, V> store, final ValueCallback<Object> callback) {
        if (skip) {
            callback.onSuccess(Success.INSTANCE);
            return;
        }

        store.getAll(keyStartsWith, fromIdx, toIdx,
                AsyncCommon.embed(callback, new Closure<List<StoreEntry<String, V>>>() {

                    @Override
                    public void apply(final List<StoreEntry<String, V>> res) {
                        if (afterGetValues.size() == 0) {
                            callback.onSuccess(res);
                            return;
                        }

                        final ArrayList<StoreEntry<String, V>> alteredResults = new ArrayList<StoreEntry<String, V>>(
                                res.size());

                        for (final StoreEntry<String, V> o : res) {
                            V value = o.value();
                            String key = o.key();
                            for (final Function<V, V> f : afterGetValues) {
                                value = f.apply(value);
                            }

                            for (final Function<String, String> f : afterGetKeys) {
                                System.out.println(this + "modify key " + key);
                                key = f.apply(key);
                                System.out.println(this + "modified key " + key);
                            }

                            alteredResults.add(new StoreEntryData<String, V>(key, value));
                        }

                        callback.onSuccess(alteredResults);

                    }
                }));

    }

    public GetAllOperation(final String keyStartsWith, final int fromIdx, final int toIdx) {
        super();
        this.keyStartsWith = keyStartsWith;
        this.afterGetValues = new LinkedList<Function<V, V>>();
        this.afterGetKeys = new LinkedList<Function<String, String>>();
        this.fromIdx = fromIdx;
        this.toIdx = toIdx;
    }

}
