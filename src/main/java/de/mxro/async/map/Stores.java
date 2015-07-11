package de.mxro.async.map;

import delight.concurrency.Concurrency;
import delight.concurrency.schedule.SequentialOperationScheduler;
import delight.functional.Closure;
import delight.functional.Function;

import java.util.Map;

import de.mxro.async.map.internal.HashMapAsyncMap;
import de.mxro.async.map.internal.decorators.StoreDecorators;

public class Stores {

    /**
     * <p>
     * If values cannot be read from the map for some reason, they are
     * automatically deleted in the map.
     * <p>
     * This can be useful for example if the serialization format of objects has
     * changed and the map only works as a cache.
     * 
     * @param forMap
     * @return
     */
    public final static <K, V> Store<K, V> purgeInvalidValues(final Store<K, V> forMap) {
        return StoreDecorators.purgeInvalidValues(forMap);
    }

    /**
     * <p>
     * Enforces that even putSync operations are performed asynchronously in the
     * background.
     * <p>
     * This is not visible to the caller though (putSync returns immediately).
     * 
     * @param delay
     * @param concurrency
     * @param decorated
     * @return
     */
    public static <K, V> Store<K, V> enforceAsynchronousPut(final int delay, final Concurrency concurrency,
            final Store<K, V> decorated) {
        return StoreDecorators.enforceAsynchronousPut(delay, concurrency, decorated);
    }

    /**
     * <p>
     * Caches writes to this map in a Java {@link Map} object and performs reads
     * from this cache whenever possible.
     * 
     * @param cache
     * @param decorated
     * @return
     */
    public static <K, V> Store<K, V> cache(final Map<K, Object> cache, final Store<K, V> decorated) {
        return StoreDecorators.cache(cache, decorated);
    }

    public static <K, V> Store<K, V> tierCaches(final Store<K, V> primaryCache, final Store<K, V> secondaryCache) {
        return StoreDecorators.tierCaches(primaryCache, secondaryCache);
    }

    public final static <K, V> Store<K, V> filterKeys(final Function<K, K> filter, final Store<K, V> decorated) {
        return StoreDecorators.filterKeys(filter, decorated);
    }

    public final static <K, V> Store<K, V> filterValues(final Function<V, V> beforeStorage,
            final Function<V, V> afterStorage, final Store<K, V> decorated) {
        return StoreDecorators.filterValues(beforeStorage, afterStorage, decorated);
    }

    public final static <K, V> Store<K, V> ignoreKeys(final Function<K, Boolean> filter, final Store<K, V> decorated) {
        return StoreDecorators.ignoreKeys(filter, decorated);
    }

    /**
     * <p>
     * Calls the {@link Store#start(de.mxro.async.callbacks.SimpleCallback)}
     * method of this map automatically when an asynchronous operation is
     * called.
     * 
     * @return
     * @see LazyStartupMap
     */
    public static <K, V> Store<K, V> lazyStartup(final Store<K, V> decorated) {
        return StoreDecorators.lazyStartup(decorated);
    }

    public static <K, V> Store<K, V> hashMap() {
        return new HashMapAsyncMap<K, V>();
    }

    public static <K, V> Store<K, V> trace(final Closure<String> messageReceiver, final Store<K, V> decorated) {
        return StoreDecorators.trace(messageReceiver, decorated);
    }

    public static <K, V> Store<K, V> assureNoConflictsWithSchedule(final SequentialOperationScheduler scheduler,
            final Store<K, V> decorated) {
        return StoreDecorators.assureNoConflictsWithScheduler(scheduler, decorated);
    }

}
