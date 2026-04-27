package sh.byv.cache.service;

import io.quarkus.cache.CacheKeyGenerator;
import jakarta.enterprise.context.ApplicationScoped;

import java.lang.reflect.Method;

@ApplicationScoped
public class WorldCacheKeyGenerator implements CacheKeyGenerator {

    @Override
    public Object generate(final Method method, final Object... methodParams) {
        final var worldId = (Long) methodParams[0];
        final var cacheName = CacheKeyUtils.getCacheName(method);
        return "world:%d:%s".formatted(worldId, cacheName);
    }
}
