package delight.keyvalue.operations;

import delight.functional.Closure;
import delight.keyvalue.StoreEntry;
import delight.keyvalue.internal.operations.ClearCacheOperation;
import delight.keyvalue.internal.operations.GetAllOperation;
import delight.keyvalue.internal.operations.RemoveAllOperation;

public class StoreOperations {

    public static <K, V> StoreOperation<K, V> clearCache() {
        return new ClearCacheOperation<K, V>();
    }

    public static <V> StoreOperation<String, V> removeAll(final String keyStartsWith) {
        return new RemoveAllOperation<V>(keyStartsWith);
    }

    public static <V> StoreOperation<String, V> getAll(final String keyStartsWith,
            final Closure<StoreEntry<String, V>> onEntry) {
        return new GetAllOperation<V>(keyStartsWith, onEntry);
    }

}
