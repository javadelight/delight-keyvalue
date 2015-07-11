package de.mxro.async.map.operations;

import de.mxro.async.map.internal.operations.ClearCacheOperation;

public class StoreOperations {

    public static StoreOperation<Object, Object> clearCache() {
        return new ClearCacheOperation<Object, Object>();
    }

}
