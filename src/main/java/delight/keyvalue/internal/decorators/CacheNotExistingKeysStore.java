package delight.keyvalue.internal.decorators;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import delight.async.callbacks.SimpleCallback;
import delight.async.callbacks.ValueCallback;
import delight.concurrency.Concurrency;
import delight.keyvalue.Store;
import delight.keyvalue.operations.StoreOperation;
import delight.trie.TrieMap;

public final class CacheNotExistingKeysStore<V> implements Store<String, V> {

	
	private final Store<String, V> decorated;

	private final TrieMap<Set<String>> missingKeyRanges;
	private final Set<String> missingKeys;

	private final void logNonExistend(String key) {
		this.missingKeys.add(key);
		this.missingKeyRanges.put(key + "/", new HashSet<String>(0));
	}

	private final void logExistend(String key) {
		this.missingKeys.remove(key);

		for (Entry<String, Set<String>> entry : this.missingKeyRanges.getSubMap(key + "/").entrySet()) {
			if (!entry.getKey().equals(key + "/")) {
				synchronized (entry.getValue()) {
					entry.getValue().add(key);
				}
			}
		}

	}

	private final boolean canExist(String key) {
		if (missingKeys.contains(key)) {
			return false;
		}

		TrieMap<Set<String>> matchingRanges = missingKeyRanges.getSubMap(key + "/");
		for (Entry<String, Set<String>> entry : matchingRanges.entrySet()) {
			
			synchronized (entry.getValue()) {
				if (entry.getValue().contains(key)) {
					return true;
				}
			}
			
		}

		return matchingRanges.size() <= 0;
	}

	@Override
	public void put(String key, V value, SimpleCallback callback) {
		logExistend(key);
		decorated.put(key, value, callback);

	}

	@Override
	public void putSync(String key, V value) {
		logExistend(key);
		decorated.putSync(key, value);

	}

	@Override
	public void get(String key, ValueCallback<V> callback) {
		if (!canExist(key)) {
			callback.onSuccess(null);
			return;
		}
		
		decorated.get(key, callback);
	}

	@Override
	public V getSync(String key) {
		if (!canExist(key)) {
			return null;
		}
		
		return decorated.getSync(key);
	}

	@Override
	public void remove(String key, SimpleCallback callback) {
		logNonExistend(key);
		decorated.remove(key, callback);

	}

	@Override
	public void removeSync(String key) {
		logNonExistend(key);
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
	public void performOperation(StoreOperation<String, V> operation, ValueCallback<Object> callback) {
		decorated.performOperation(operation, callback);
	}

	public CacheNotExistingKeysStore(Concurrency concurrency, Store<String, V> decorated) {
		super();
		
		this.decorated = decorated;

		this.missingKeyRanges = new TrieMap<Set<String>>(concurrency);
		this.missingKeys = concurrency.newCollection().newThreadSafeSet(String.class);
	}

}
