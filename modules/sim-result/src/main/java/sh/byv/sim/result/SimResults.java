package sh.byv.sim.result;

import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.value.ValueCommands;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class SimResults {

    final SimResultsConfig config;

    final ValueCommands<String, SimResult> results;

    final String prefix;

    public SimResults(final SimResultsConfig config, final RedisDataSource redis) {
        this.config = config;

        results = redis.value(SimResult.class);

        prefix = config.prefix();
    }

    public void setSimResult(final long simId, final long tick, final SimResult result) {
        final var ttl = config.ttl().getSeconds();
        results.setex(getSimResultKey(simId, tick), ttl, result);
    }

    public SimResult getSimResult(final long simId, final long tick) {
        return results.get(getSimResultKey(simId, tick));
    }

    private String getSimResultKey(final long simId, final long tick) {
        // omgstate:sim:1:tick:2:sim-result
        return "%s:sim:%d:tick:%d:sim-result".formatted(prefix, simId, tick);
    }
}
