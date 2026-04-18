package sh.byv.cache.service;

import io.quarkus.cache.CacheInvalidate;
import io.quarkus.cache.CacheKeyGenerator;
import io.quarkus.cache.CacheResult;
import jakarta.enterprise.context.ApplicationScoped;

import java.lang.reflect.Method;

@ApplicationScoped
public class ServerCacheKeyGenerator implements CacheKeyGenerator {

    @Override
    public Object generate(final Method method, final Object... methodParams) {
        final var serverName = (String) methodParams[0];
        final var cacheName = getCacheName(method);
        return "server:%s:%s".formatted(serverName, cacheName);
    }

    private String getCacheName(final Method method) {
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
