package sh.byv.state.service;

import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.sortedset.SortedSetCommands;
import io.quarkus.redis.datasource.value.ValueCommands;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class StateService {

    final StateConfig config;

    final SortedSetCommands<String, Long> ssets;
    final ValueCommands<String, Object> objects;
    final ValueCommands<String, Long> longs;

    final String prefix;

    public StateService(final StateConfig config, final RedisDataSource redis) {
        this.config = config;

        ssets = redis.sortedSet(Long.class);
        objects = redis.value(Object.class);
        longs = redis.value(Long.class);

        prefix = config.prefix();
    }

    public void setSimState(final long simId, final long tick, final Object state) {
        final var ttl = config.simState().ttl().getSeconds();
        objects.setex(getSimStateKey(simId, tick), ttl, state);
    }

    public Object getSimState(final long simId, final long tick) {
        return objects.get(getSimStateKey(simId, tick));
    }

    private String getSimStateKey(final long simId, final long tick) {
        // omgc:sim:1:tick:2:sim-state
        return "%s:sim:%d:tick:%d:sim-state".formatted(prefix, simId, tick);
    }

    public void setZoneState(final long zoneId, final long tick, final Object state) {
        final var ttl = config.zoneState().ttl().getSeconds();
        objects.setex(getZoneStateKey(zoneId, tick), ttl, state);
    }

    public Object getZoneState(final long zoneId, final long tick) {
        return objects.get(getZoneStateKey(zoneId, tick));
    }

    private String getZoneStateKey(final long zoneId, final long tick) {
        // omgc:zone:1:tick:2:zone-state
        return "%s:zone:%d:tick:%d:zone-state".formatted(prefix, zoneId, tick);
    }

    public long getTickTick(final long zoneId) {
        final var value = longs.get(getTickNumberKey(zoneId));
        return value != null ? value : 0L;
    }

    public long incrTickNumber(final long zoneId) {
        return longs.incr(getTickNumberKey(zoneId));
    }

    private String getTickNumberKey(final long zoneId) {
        // omgc:zone:1:tick
        return "%s:zone:%d:tick-number".formatted(prefix, zoneId);
    }

    public void addExecutedTick(final long zoneId, final long tick) {
        ssets.zadd(getExecutedTicksKey(zoneId), tick, tick);
    }

    public Long getLatestExecutedTick(final long zoneId) {
        final var values = ssets.zrange(getExecutedTicksKey(zoneId), -1, -1);
        if (values.isEmpty()) {
            return null;
        } else {
            return values.getFirst();
        }
    }

    private String getExecutedTicksKey(final long zoneId) {
        // omgc:zone:1:executed-ticks
        return "%s:zone:%d:executed-ticks".formatted(prefix, zoneId);
    }
}