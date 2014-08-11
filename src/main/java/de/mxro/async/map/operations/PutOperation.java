package de.mxro.async.map.operations;

import de.mxro.async.callbacks.SimpleCallback;
import de.mxro.async.map.AsyncMap;

/**
 * An object representation of a put operation on an asynchronous map.
 * 
 * @author <a href="http://www.mxro.de">Max Rohde</a>
 *
 * @param <V>
 * @see AsyncMap
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

	public PutOperation(K key, V value, SimpleCallback callback) {
		super();
		this.key = key;
		this.value = value;
		this.callback = callback;
	}

}
