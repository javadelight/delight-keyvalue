package de.mxro.async.map.operations;

public class StoreOperations {

    public static StoreOperation<Object, Object> clearCache() {
        return new ClearCacheOperation<Object, Object>();
    }

}
