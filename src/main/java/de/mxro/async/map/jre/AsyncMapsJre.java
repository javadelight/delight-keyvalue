package de.mxro.async.map.jre;

import java.util.Collections;
import java.util.WeakHashMap;

import de.mxro.async.map.Store;
import de.mxro.async.map.Stores;
import de.mxro.async.map.jre.internal.SplitWorkerThreadsMapConnection;

public class AsyncMapsJre {

	public static final <K, V> Store<K, V> cacheWithWeakReferences(
			Store<K, V> decorated) {
		return Stores.cache(
				Collections.synchronizedMap(new WeakHashMap<K, Object>()),
				decorated);
	}

	public static final <K, V> Store<K, V> divideWork(int workerThreads,
			Store<K, V> decorated) {
		return new SplitWorkerThreadsMapConnection<K, V>(decorated,
				workerThreads);
	}
	

}
