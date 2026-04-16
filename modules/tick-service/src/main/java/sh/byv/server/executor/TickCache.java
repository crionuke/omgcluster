package sh.byv.server.executor;

import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.value.ValueCommands;
import jakarta.enterprise.context.ApplicationScoped;
import sh.byv.server.entity.ServerConfig;

@ApplicationScoped
public class TickCache {

    static private String TICK_KEY = "omgc:tick";

    final ServerConfig config;

    final ValueCommands<String, Long> longCommands;

    public TickCache(final ServerConfig config, final RedisDataSource redis) {
        this.config = config;

        longCommands = redis.value(Long.class);
    }

    public long getTick() {
        return longCommands.get(TICK_KEY);
    }

    public long incrTick() {
        return longCommands.incr(TICK_KEY);
    }
}
