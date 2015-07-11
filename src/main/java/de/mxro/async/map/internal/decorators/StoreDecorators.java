package de.mxro.async.map.internal.decorators;

import delight.concurrency.Concurrency;
import delight.concurrency.schedule.SequentialOperationScheduler;
import delight.functional.Closure;
import delight.functional.Function;

import java.util.Map;

import de.mxro.async.map.Store;

public class StoreDecorators {

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
        return new EnforceAsynchronousPutMap<K, V>(delay, concurrency, decorated);
    }

    /**
     * <p>
     * Attempts to only perform operations on the map when the scheduler is
     * idle.
     * 
     * @param scheduler
     * @param decorated
     * @return
     */
    public static <K, V> Store<K, V> assureNoConflictsWithScheduler(final SequentialOperationScheduler scheduler,
            final Store<K, V> decorated) {
        return new AssureNoConflictsWithSchedulerIdle<K, V>(scheduler, decorated);
    }

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
        return new PurgeInvalidValuesMap<K, V>(forMap);
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
        return new SimpleCachedMap<K, V>(cache, decorated);
    }

    /**
     * <p>
     * Caches writes to this map in another map.
     * 
     * @param primaryCache
     * @param secondaryCache
     * @return
     */
    public static <K, V> Store<K, V> tierCaches(final Store<K, V> primaryCache, final Store<K, V> secondaryCache) {
        return new TieredCachesMap<K, V>(primaryCache, secondaryCache);
    }

    public final static <K, V> Store<K, V> filterKeys(final Function<K, K> filter, final Store<K, V> decorated) {
        return new KeyFilterMap<K, V>(filter, decorated);
    }

    public final static <K, V> Store<K, V> filterValues(final Function<V, V> beforeStorage,
            final Function<V, V> afterStorage, final Store<K, V> decorated) {
        return new ValueFilterMap<K, V>(beforeStorage, afterStorage, decorated);
    }

    public final static <K, V> Store<K, V> ignoreKeys(final Function<K, Boolean> filter, final Store<K, V> decorated) {
        return new IgnoreKeysMap<K, V>(filter, decorated);
    }

    /**
     * <p>
     * Calls the {@link Store#start(de.mxro.async.callbacks.SimpleCallback)}
     * method of this map automatically when an asynchronous operation is
     * called.
     * 
     * @param decorated
     * @return
     * @see LazyStartupMap
     */
    public static <K, V> Store<K, V> lazyStartup(final Store<K, V> decorated) {
        return new LazyStartupMap<K, V>(decorated);
    }

    public static <K, V> Store<K, V> trace(final Closure<String> messageReceiver, final Store<K, V> decorated) {
        return new TraceMap<K, V>(messageReceiver, decorated);
    }
}
