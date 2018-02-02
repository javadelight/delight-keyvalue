package delight.keyvalue.internal.decorators;

import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import delight.async.AsyncCommon;
import delight.async.callbacks.SimpleCallback;
import delight.async.callbacks.ValueCallback;
import delight.concurrency.Concurrency;
import delight.functional.Closure;
import delight.keyvalue.Store;
import delight.keyvalue.operations.StoreOperation;
import delight.simplelog.Log;
import delight.trie.TrieMap;

public final class CacheNotExistingKeysStore<V> implements Store<String, V> {

	private final static boolean ENABLE_TRACE = false;

	private final Store<String, V> decorated;

	private final TrieMap<Set<String>> missingKeyRanges;
	private final Set<String> missingKeys;

	private final void logNonExistent(String key) {
		if (ENABLE_TRACE) {
			Log.println(this, "Log not existent: " + key);
		}
		this.missingKeys.add(key);

		List<Set<String>> parents = missingKeyRanges.getValuesOnPath(key + "/");
		
		for (Set<String> parent : parents) {
			synchronized (parent) {
				parent.remove(key);
			}
		}

		this.missingKeyRanges.put(key + "/", new HashSet<String>(0));

	}

	private final void logExistent(String key) {
		if (ENABLE_TRACE) {
			Log.println(this, "Log exist: " + key);
		}
		
		this.missingKeys.remove(key);
		
		if (missingKeyRanges.size() > 200) {
			missingKeyRanges.clear();
			return;
		}

		

		List<Set<String>> parents = missingKeyRanges.getValuesOnPath(key + "/");

		// to prevent too many ranges having to be updated
		if (parents.size() > 20) {
			missingKeyRanges.clear();
			return;
		}

		for (Set<String> parent : parents) {
			synchronized (parent) {
				parent.add(key);
			}
		}

		

	}

	private final boolean canExist(String key) {
		if (missingKeys.contains(key)) {
			if (ENABLE_TRACE) {
				Log.println(this, "Miss from cache: " + key);
			}
			return false;
		}

		List<Set<String>> parents = missingKeyRanges.getValuesOnPath(key + "/");
		if (parents.size() == 0) {
			return true;
		}

		for (Set<String> parent : parents) {
			synchronized (parent) {
				if (parent.contains(key)) {
					return true;
				}
			}
		}
		if (ENABLE_TRACE) {
			Log.println(this, "Miss from cache range: " + key);
		}
		logNonExistent(key);
		// some parent is marked as being non existent and this no exception has been recorded.
		return false;
	}

	@Override
	public void put(String key, V value, SimpleCallback callback) {
		logExistent(key);
		decorated.put(key, value, callback);

	}

	@Override
	public void putSync(String key, V value) {
		logExistent(key);
		decorated.putSync(key, value);

	}

	@Override
	public void get(final String key, final ValueCallback<V> callback) {
		if (!canExist(key)) {
			callback.onSuccess(null);
			return;
		}

		decorated.get(key, AsyncCommon.embed(callback, new Closure<V>() {

			@Override
			public void apply(V v) {
				if (v == null) {
					logNonExistent(key);
				}

				callback.onSuccess(v);
			}

		}));
	}

	@Override
	public V getSync(String key) {
		if (!canExist(key)) {
			return null;
		}

		V v = decorated.getSync(key);
		if (v == null) {
			logNonExistent(key);
		}
		return v;
	}

	@Override
	public void remove(String key, SimpleCallback callback) {
		logNonExistent(key);
		decorated.remove(key, callback);

	}

	@Override
	public void removeSync(String key) {
		logNonExistent(key);
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
