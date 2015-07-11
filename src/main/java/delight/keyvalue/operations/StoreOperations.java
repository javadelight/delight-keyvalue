package delight.keyvalue.operations;

import delight.keyvalue.internal.operations.ClearCacheOperation;

public class StoreOperations {

    public static StoreOperation<Object, Object> clearCache() {
        return new ClearCacheOperation<Object, Object>();
    }

}
