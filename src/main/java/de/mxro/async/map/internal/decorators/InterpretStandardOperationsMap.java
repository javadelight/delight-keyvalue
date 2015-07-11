package de.mxro.async.map.internal.decorators;

import delight.async.callbacks.SimpleCallback;
import delight.async.callbacks.ValueCallback;

import de.mxro.async.map.Store;
import de.mxro.async.map.operations.GetOperation;
import de.mxro.async.map.operations.StoreOperation;
import de.mxro.async.map.operations.FullPutOperation;
import de.mxro.async.map.operations.RemoveOperation;

/**
 * <p>Forwards standard operations (put, get, remove) to the methods on this map.
 * 
 * @author <a href="http://www.mxro.de">Max Rohde</a>
 *
 * @param <K>
 * @param <V>
 */
class InterpretStandardOperationsMap<K, V> implements Store<K, V> {

	private final Store<K, V> decorated;

	@Override
	public void put(K key, V value, SimpleCallback callback) {
		decorated.put(key, value, callback);
	}

	@Override
	public void get(K key, ValueCallback<V> callback) {
		decorated.get(key, callback);
	}

	@Override
	public void remove(K key, SimpleCallback callback) {
		decorated.remove(key, callback);
	}

	@Override
	public V getSync(K key) {
		
		return decorated.getSync(key);
	}

	@Override
	public void putSync(K key, V value) {
		decorated.putSync(key, value);
	}

	@Override
	public void removeSync(K key) {
		decorated.removeSync(key);
	}

	@Override
	public void start(SimpleCallback callback) {
		decorated.start(callback);
	}

	@Override
	public void stop(SimpleCallback callback) {
		decorated.stop(callback);
	}

	@Override
	public void commit(SimpleCallback callback) {
		decorated.commit(callback);
	}

	@Override
	public void performOperation(StoreOperation operation) {
		if (operation instanceof GetOperation) {

			@SuppressWarnings("unchecked")
			GetOperation<K, V> getOperation = (GetOperation<K, V>) operation;
			this.get(getOperation.getKey(), getOperation.getCallback());
			return;
		}

		if (operation instanceof FullPutOperation) {
			@SuppressWarnings("unchecked")
			FullPutOperation<K, V> putOperation = (FullPutOperation<K, V>) operation;

			this.put(putOperation.getKey(), putOperation.getValue(),
					putOperation.getCallback());
			return;
		}
		
		if (operation instanceof RemoveOperation) {
			@SuppressWarnings("unchecked")
			RemoveOperation<K> removeOperation = (RemoveOperation<K>) operation;
			
			this.remove(removeOperation.getKey(), removeOperation.getCallback());
			
		}
		
		this.decorated.performOperation(operation);
	}

	public InterpretStandardOperationsMap(Store<K, V> decorated) {
		super();
		this.decorated = decorated;
	}

}
