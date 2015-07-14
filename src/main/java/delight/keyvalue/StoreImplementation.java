package delight.keyvalue;

import delight.async.callbacks.SimpleCallback;
import delight.async.callbacks.ValueCallback;
import delight.functional.Closure;

import java.util.List;

public interface StoreImplementation<K, V> extends Store<K, V> {

    public void clearCache();

    public void removeAll(String keyStartsWith, SimpleCallback callback);

    public void getAll(String keyStartsWith, final Closure<StoreEntry<K, V>> onEntry, final SimpleCallback onCompleted);

    public void count(String keyStartsWith, final ValueCallback<Integer> callback);

    public void get(List<K> keys, ValueCallback<List<V>> callback);

}
