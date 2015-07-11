package delight.keyvalue.operations;

import delight.async.callbacks.SimpleCallback;

/**
 * An object representation of a put operation on an asynchronous map.
 * 
 * @author <a href="http://www.mxro.de">Max Rohde</a>
 *
 * @param <V>
 * @see PureAsyncMap
 */
public class PutOperation<K, V> {

    private final K key;
    private final V value;
    private final SimpleCallback callback;

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public SimpleCallback getCallback() {
        return callback;
    }

    public PutOperation(final K key, final V value, final SimpleCallback callback) {
        super();
        this.key = key;
        this.value = value;
        this.callback = callback;
    }

}
