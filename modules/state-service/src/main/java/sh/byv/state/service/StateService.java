package sh.byv.state.service;

import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.hash.HashCommands;
import io.quarkus.redis.datasource.sortedset.SortedSetCommands;
import io.quarkus.redis.datasource.value.ValueCommands;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class StateService {

    final StateConfig config;

    final ValueCommands<String, CachedState> cache;
    final SortedSetCommands<String, Long> ssets;
    final ValueCommands<String, Object> objects;
    final ValueCommands<String, Long> longs;

    final String prefix;

    public StateService(final StateConfig config, final RedisDataSource redis) {
        this.config = config;

        cache = redis.value(CachedState.class);
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

    public void setZoneState(final long zoneId, final long tick, final Object state) {
        final var ttl = config.zoneState().ttl().getSeconds();
        objects.setex(getZoneStateKey(zoneId, tick), ttl, state);
        cache.set(getCachedStateKey(zoneId), new CachedState(tick, state));
    }

    public Object getZoneState(final long zoneId, final long tick) {
        return objects.get(getZoneStateKey(zoneId, tick));
    }

    public CachedState getCachedState(final long zoneId) {
        return cache.get(getCachedStateKey(zoneId));
    }

    public long getZoneTick(final long zoneId) {
        final var value = longs.get(getZoneTickKey(zoneId));
        return value != null ? value : 0L;
    }

    public long incrZoneTick(final long zoneId) {
        return longs.incr(getZoneTickKey(zoneId));
    }

    private String getSimStateKey(final long simId, final long tick) {
        // omgstate:sim:1:tick:2:sim-state
        return "%s:sim:%d:tick:%d:sim-state".formatted(prefix, simId, tick);
    }

    private String getZoneStateKey(final long zoneId, final long tick) {
        // omgstate:zone:1:tick:2:zone-state
        return "%s:zone:%d:tick:%d:zone-state".formatted(prefix, zoneId, tick);
    }

    private String getCachedStateKey(final long zoneId) {
        // omgstate:zone:1:cached-state
        return "%s:zone:%d:cached-state".formatted(prefix, zoneId);
    }

    private String getZoneTickKey(final long zoneId) {
        // omgstate:zone:1:zone-tick
        return "%s:zone:%d:zone-tick".formatted(prefix, zoneId);
    }
}