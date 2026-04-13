package sh.byv.cache;

import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.value.ValueCommands;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class CacheService {

    final ValueCommands<String, Long> tickCommands;

    public CacheService(final RedisDataSource redis) {
        tickCommands = redis.value(Long.class);
    }

    public long nextTick(final Long zoneId) {
        return tickCommands.incr("zone:%d".formatted(zoneId));
    }

    public long currentTick(final Long zoneId) {
        return tickCommands.get("zone:%d".formatted(zoneId));
    }
}
