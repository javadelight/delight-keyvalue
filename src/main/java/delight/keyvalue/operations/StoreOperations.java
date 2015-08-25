package delight.keyvalue.operations;

import delight.keyvalue.internal.operations.ClearCacheOperation;
import delight.keyvalue.internal.operations.CountOperation;
import delight.keyvalue.internal.operations.GetAllOperation;
import delight.keyvalue.internal.operations.MultiGetOperation;
import delight.keyvalue.internal.operations.RemoveAllOperation;

import java.util.List;

public class StoreOperations {

    public static <K, V> StoreOperation<K, V> clearCache() {
        return new ClearCacheOperation<K, V>();
    }

    public static <K, V> StoreOperation<K, V> removeAll(final String keyStartsWith) {
        return (StoreOperation<K, V>) new RemoveAllOperation<V>(keyStartsWith);
    }

    public static <K, V> StoreOperation<K, V> getAll(final String keyStartsWith, final int fromIdx, final int toIdx) {
        return (StoreOperation<K, V>) new GetAllOperation<V>(keyStartsWith, fromIdx, toIdx);
    }

    public static <K, V> StoreOperation<K, V> count(final String keyStartsWith) {
        return (StoreOperation<K, V>) new CountOperation<V>(keyStartsWith);
    }

    public static <K, V> StoreOperation<K, V> getAll(final List<K> keys) {
        return new MultiGetOperation<K, V>(keys);
    }

}
