package de.mxro.async.map.internal.decorators;

import de.mxro.async.callbacks.SimpleCallback;
import de.mxro.async.callbacks.ValueCallback;
import de.mxro.async.map.AsyncMap2;

/**
 * <p>Use in case seralizations can get outdated in a cache.
 * 
 * @author <a href="http://www.mxro.de/">Max Rohde</a>
 * 
 */
public final class PurgeInvalidValuesMap<K, V> implements AsyncMap2<K, V> {

	private final AsyncMap2<K, V> decorated;

	@Override
	public void put(K key, V value, SimpleCallback callback) {
		decorated.put(key, value, callback);
	}

	@Override
	public void get(K key, ValueCallback<V> callback) {
		get(key, callback, true);
	}

	
	
	private final void get(K key, ValueCallback<V> callback, boolean firstTry) {
		try {
			decorated.get(key, callback);		
		} catch (Throwable t) {
			if (!firstTry) {
				throw new RuntimeException(t);
			}

			deleteAndReget(key, callback);
		}

	}

	private final void deleteAndReget(final K key, final ValueCallback<V> callback) {
		remove(key, new SimpleCallback() {

			@Override
			public void onFailure(Throwable t) {
				callback.onFailure(t);
			}

			@Override
			public void onSuccess() {
				get(key, callback, false);
			}
		});

		 
	}


	
	
	@Override
	public void remove(K key, SimpleCallback callback) {
		decorated.remove(key, callback);
	}

	
	@Override
	public V getSync(K key) {
		try {
			V res = decorated.getSync(key);
			
			if (res == null) {
				return removeAndRegetSync(key);
			}
			
			return res;
		} catch (Throwable t) {
			return removeAndRegetSync(key);
		}
		
	}

	public V removeAndRegetSync(K key) {
		remove(key, new SimpleCallback() {
			
			@Override
			public void onFailure(Throwable t) {
				throw new RuntimeException(t);
			}
			
			@Override
			public void onSuccess() {
				
			}
		});
		
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

	public PurgeInvalidValuesMap(AsyncMap2<K, V> decorated) {
		super();
		this.decorated = decorated;
	}

}
