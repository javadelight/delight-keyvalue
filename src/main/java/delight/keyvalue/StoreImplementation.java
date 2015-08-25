package delight.keyvalue;

import delight.async.callbacks.SimpleCallback;
import delight.async.callbacks.ValueCallback;

import java.util.List;

public interface StoreImplementation<K, V> extends Store<K, V> {

    public void clearCache();

    public void removeAll(String keyStartsWith, SimpleCallback callback);

    public void getAll(String keyStartsWith, final int fromIdx, final int toIdx,
            final ValueCallback<List<StoreEntry<K, V>>> callback);

    public void count(String keyStartsWith, final ValueCallback<Integer> callback);

    public void get(List<K> keys, ValueCallback<List<V>> callback);

}
