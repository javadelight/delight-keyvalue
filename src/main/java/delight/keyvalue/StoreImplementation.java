package delight.keyvalue;

import delight.async.callbacks.SimpleCallback;
import delight.async.callbacks.ValueCallback;
import delight.functional.Function;

public interface StoreImplementation<K, V> extends Store<K, V> {

    public void clearCache();

    public void removeAll(Function<K, Boolean> elementTest, SimpleCallback callback);

    public void getAll(final Function<K, Boolean> elementTest, final ValueCallback<StoreEntry<K, V>> onEntry,
            final SimpleCallback onCompleted);

}
