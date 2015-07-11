package de.mxro.async.map;

public interface StoreImplementation<K, V> extends Store<K, V> {

    public void clearCache();

}
