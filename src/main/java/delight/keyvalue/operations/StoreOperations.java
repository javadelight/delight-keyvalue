package delight.keyvalue.operations;

import delight.functional.Closure;
import delight.keyvalue.StoreEntry;
import delight.keyvalue.internal.operations.ClearCacheOperation;
import delight.keyvalue.internal.operations.CountOperation;
import delight.keyvalue.internal.operations.GetAllOperation;
import delight.keyvalue.internal.operations.RemoveAllOperation;

public class StoreOperations {

    public static <K, V> StoreOperation<K, V> clearCache() {
        return new ClearCacheOperation<K, V>();
    }

    public static <K, V> StoreOperation<K, V> removeAll(final String keyStartsWith) {
        return (StoreOperation<K, V>) new RemoveAllOperation<V>(keyStartsWith);
    }

    public static <K, V> StoreOperation<K, V> getAll(final String keyStartsWith,
            final Closure<StoreEntry<String, V>> onEntry) {
        return (StoreOperation<K, V>) new GetAllOperation<V>(keyStartsWith, onEntry);
    }

    public static <K, V> StoreOperation<K, V> count(final String keyStartsWith) {
        return (StoreOperation<K, V>) new CountOperation<V>(keyStartsWith);
    }

}
