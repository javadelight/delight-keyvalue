package delight.keyvalue.jre;

import delight.keyvalue.Store;
import delight.keyvalue.Stores;
import delight.keyvalue.jre.internal.MultiGetStore;
import delight.keyvalue.jre.internal.SplitWorkerThreadsMapConnection;

import java.util.WeakHashMap;

public class StoresJre {

    public static final <K, V> Store<K, V> cacheWithWeakReferences(final Store<K, V> decorated) {
        return Stores.cache(new WeakHashMap<K, Object>(), decorated);
    }

    public static final <K, V> Store<K, V> divideWork(final int workerThreads, final Store<K, V> decorated) {
        return new SplitWorkerThreadsMapConnection<K, V>(decorated, workerThreads);
    }

    public static final <K, V> Store<K, V> forceBatchGets(final int maxDelayInMs, final Store<K, V> decorated) {
        return new MultiGetStore<K, V>(maxDelayInMs, decorated);
    }

}
