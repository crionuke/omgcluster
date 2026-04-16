package sh.byv.zone.entity;

import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.value.ValueCommands;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ZoneCache {

    private static String getKey(final long zoneId) {
        return "omgc:zone:%d:tick".formatted(zoneId);
    }

    final ValueCommands<String, Long> longCommands;

    public ZoneCache(final RedisDataSource redis) {
        this.longCommands = redis.value(Long.class);
    }

    public long getZoneTick(final long zoneId) {
        return longCommands.get(getKey(zoneId));
    }

    public long incrZoneTick(final long zoneId) {
        return longCommands.incr(getKey(zoneId));
    }
}
