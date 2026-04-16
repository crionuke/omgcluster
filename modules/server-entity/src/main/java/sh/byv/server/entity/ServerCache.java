package sh.byv.server.entity;

import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.value.ValueCommands;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ServerCache {

    private static String getKey(final String server) {
        return "omgc:server:%d:tick".formatted(server);
    }

    final ValueCommands<String, Long> longCommands;

    public ServerCache(final RedisDataSource redis) {
        this.longCommands = redis.value(Long.class);
    }

    public long getSeverTick(final String server) {
        return longCommands.get(getKey(server));
    }

    public long incrServerTick(final String server) {
        return longCommands.incr(getKey(server));
    }
}
