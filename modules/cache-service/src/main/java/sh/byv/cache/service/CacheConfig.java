package sh.byv.cache.service;

import io.smallrye.config.ConfigMapping;

import java.time.Duration;

@ConfigMapping(prefix = "quarkus.cache.redis")
public interface CacheConfig {

    String prefix();

    StateConfig simState();

    StateConfig zoneState();

    interface StateConfig {
        Duration ttl();
    }
}
