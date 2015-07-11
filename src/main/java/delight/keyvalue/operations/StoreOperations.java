package delight.keyvalue.operations;

import delight.keyvalue.internal.operations.ClearCacheOperation;

public class StoreOperations {

    public static <K, V> StoreOperation<K, V> clearCache() {
        return new ClearCacheOperation<K, V>();
    }

}
