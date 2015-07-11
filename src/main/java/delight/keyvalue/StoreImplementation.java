package delight.keyvalue;

import delight.async.callbacks.SimpleCallback;
import delight.async.callbacks.ValueCallback;

public interface StoreImplementation<K, V> extends Store<K, V> {

    public void clearCache();

    public void removeAll(K commonKeyElement, SimpleCallback callback);

    public void getAll(K commonKeyElement, ValueCallback<StoreEntry<K, V>> entry, SimpleCallback onCompleted);

}
