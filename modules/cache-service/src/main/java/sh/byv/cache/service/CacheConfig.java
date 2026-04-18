package sh.byv.cache.service;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "quarkus.cache.redis")
public interface CacheConfig {

    String prefix();
}
