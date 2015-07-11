package de.mxro.async.map.operations;

import delight.async.callbacks.SimpleCallback;

import de.mxro.async.map.Store;

/**
 * An object representation of a remove operation on an {@link Store}.
 * 
 * @author <a href="http://www.mxro.de">Max Rohde</a>
 *
 */
public class RemoveOperation<K> implements StoreOperation {

	private final K key;
	private final SimpleCallback callback;

	public K getKey() {
		return key;
	}

	public SimpleCallback getCallback() {
		return callback;
	}

	public RemoveOperation(K key, SimpleCallback callback) {
		super();
		this.key = key;
		this.callback = callback;
	}

}
