package delight.keyvalue;

import delight.async.callbacks.SimpleCallback;
import delight.async.callbacks.ValueCallback;
import delight.functional.Function;

public interface StoreImplementation<K, V> extends Store<K, V> {

    public void clearCache();

    public void removeAll(K commonKeyElement, SimpleCallback callback);

    public void getAll(final Function<K, V> elementTest, final ValueCallback<StoreEntry<K, V>> onEntry,
            final SimpleCallback onCompleted);

}
