package sh.byv.cache.service;

import io.quarkus.cache.CacheInvalidate;
import io.quarkus.cache.CacheResult;

import java.lang.reflect.Method;

class CacheKeyUtils {

    static String getCacheName(final Method method) {
        final var cacheResult = method.getAnnotation(CacheResult.class);
        if (cacheResult != null) {
            return cacheResult.cacheName();
        }
        final var cacheInvalidate = method.getAnnotation(CacheInvalidate.class);
        if (cacheInvalidate != null) {
            return cacheInvalidate.cacheName();
        }
        throw new IllegalStateException("No cache annotation found on method: " + method.getName());
    }
}