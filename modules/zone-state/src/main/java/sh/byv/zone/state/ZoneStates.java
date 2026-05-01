package sh.byv.zone.state;

import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.value.ValueCommands;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class ZoneStates {

    final ZoneStatesConfig config;

    final ValueCommands<String, CachedState> cache;
    final ValueCommands<String, ZoneState> states;
    final ValueCommands<String, Long> longs;

    final String prefix;

    public ZoneStates(final ZoneStatesConfig config, final RedisDataSource redis) {
        this.config = config;

        cache = redis.value(CachedState.class);
        states = redis.value(ZoneState.class);
        longs = redis.value(Long.class);

        prefix = config.prefix();
    }

    public void setZoneState(final long zoneId, final long tick, final ZoneState state) {
        final var ttl = config.ttl().getSeconds();
        states.setex(getZoneStateKey(zoneId, tick), ttl, state);
        cache.set(getCachedStateKey(zoneId), new CachedState(tick, state));
    }

    public ZoneState getZoneState(final long zoneId, final long tick) {
        return states.get(getZoneStateKey(zoneId, tick));
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
