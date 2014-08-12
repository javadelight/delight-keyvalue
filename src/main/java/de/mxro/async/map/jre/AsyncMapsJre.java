package de.mxro.async.map.jre;

import java.util.Collections;
import java.util.WeakHashMap;

import de.mxro.async.map.AsyncMap2;
import de.mxro.async.map.AsyncMap;
import de.mxro.async.map.internal.decorators.MapCacheMapConnection;
import de.mxro.async.map.jre.internal.SplitWorkerThreadsMapConnection;

public class AsyncMapsJre {

	public static final <K, V> AsyncMap2<K, V> cacheWithWeakReferences(
			AsyncMap2<K, V> decorated) {
		return new MapCacheMapConnection<K, V>(
				Collections.synchronizedMap(new WeakHashMap<K, Object>()),
				decorated);
	}

	public static final <K, V> AsyncMap<K, V> divideWork(int workerThreads,
			AsyncMap<K, V> decorated) {
		return new SplitWorkerThreadsMapConnection<K, V>(decorated,
				workerThreads);
	}

}
