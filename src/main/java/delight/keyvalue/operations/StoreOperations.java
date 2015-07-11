package delight.keyvalue.operations;

import delight.keyvalue.internal.operations.ClearCacheOperation;
import delight.keyvalue.internal.operations.RemoveAllOperation;

public class StoreOperations {

    public static <K, V> StoreOperation<K, V> clearCache() {
        return new ClearCacheOperation<K, V>();
    }

    public static <V> StoreOperation<String, V> removeAll(final String keyStartsWith) {
        return new RemoveAllOperation<V>(keyStartsWith);
    }

}
