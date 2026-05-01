package sh.byv.sim.results;

import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.value.ValueCommands;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class SimResults {

    final SimResultsConfig config;

    final ValueCommands<String, Object> objects;

    final String prefix;

    public SimResults(final SimResultsConfig config, final RedisDataSource redis) {
        this.config = config;

        objects = redis.value(Object.class);

        prefix = config.prefix();
    }

    public void setSimResult(final long simId, final long tick, final Object result) {
        final var ttl = config.ttl().getSeconds();
        objects.setex(getSimResultKey(simId, tick), ttl, result);
    }

    public Object getSimResult(final long simId, final long tick) {
        return objects.get(getSimResultKey(simId, tick));
    }

    private String getSimResultKey(final long simId, final long tick) {
        // omgstate:sim:1:tick:2:sim-result
        return "%s:sim:%d:tick:%d:sim-result".formatted(prefix, simId, tick);
    }
}
