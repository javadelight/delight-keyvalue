package delight.keyvalue.jre;

import delight.keyvalue.Store;
import delight.keyvalue.Stores;
import delight.keyvalue.jre.internal.SplitWorkerThreadsMapConnection;

import java.util.Collections;
import java.util.WeakHashMap;

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
