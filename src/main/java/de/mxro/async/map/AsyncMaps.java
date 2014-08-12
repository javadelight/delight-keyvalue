package de.mxro.async.map;

import java.util.Map;

import de.mxro.async.map.internal.decorators.DelayPutConnection;
import de.mxro.async.map.internal.decorators.MapCacheMapConnection;
import de.mxro.async.map.internal.decorators.PurgeInvalidValuesMap;
import de.mxro.concurrency.Concurrency;

public class AsyncMaps {

	public final static <K, V> AsyncMap2<K, V> purgeInvalidValues(
			AsyncMap2<K, V> forMap) {
		return new PurgeInvalidValuesMap<K, V>(forMap);
	}

	public static <K, V> AsyncMap<K, V> delayPutConnection(int delay,
			Concurrency concurrency, AsyncMap<K, V> decorated) {
		return new DelayPutConnection<K, V>(delay, concurrency, decorated);
	}

	public static <K, V> AsyncMap2<K,V> cacheInMapConnection(Map<K, Object> cache,
			AsyncMap2<K, V> decorated) {
		return new MapCacheMapConnection<K, V>(cache, decorated);
	}

	
	
}
