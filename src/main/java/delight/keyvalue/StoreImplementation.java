package delight.keyvalue;

import delight.async.callbacks.SimpleCallback;
import delight.functional.Closure;

public interface StoreImplementation<K, V> extends Store<K, V> {

    public void clearCache();

    public void removeAll(String keyStartsWith, SimpleCallback callback);

    public void getAll(String keyStartsWith, final Closure<StoreEntry<K, V>> onEntry, final SimpleCallback onCompleted);

}
