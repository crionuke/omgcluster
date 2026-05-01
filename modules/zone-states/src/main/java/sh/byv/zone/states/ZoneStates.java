package sh.byv.zone.states;

import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.value.ValueCommands;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class ZoneStates {

    final ZoneStatesConfig config;

    final ValueCommands<String, ZoneState> cache;
    final ValueCommands<String, Object> objects;
    final ValueCommands<String, Long> longs;

    final String prefix;

    public ZoneStates(final ZoneStatesConfig config, final RedisDataSource redis) {
        this.config = config;

        cache = redis.value(ZoneState.class);
        objects = redis.value(Object.class);
        longs = redis.value(Long.class);

        prefix = config.prefix();
    }

    public void setTickState(final long zoneId, final long tick, final Object state) {
        final var ttl = config.ttl().getSeconds();
        objects.setex(getTickStateKey(zoneId, tick), ttl, state);
        cache.set(getZoneStateKey(zoneId), new ZoneState(tick, state));
    }

    public Object getTickState(final long zoneId, final long tick) {
        return objects.get(getTickStateKey(zoneId, tick));
    }

    public ZoneState getZoneState(final long zoneId) {
        return cache.get(getZoneStateKey(zoneId));
    }

    public long getZoneTick(final long zoneId) {
        final var value = longs.get(getZoneTickKey(zoneId));
        return value != null ? value : 0L;
    }

    public long incrZoneTick(final long zoneId) {
        return longs.incr(getZoneTickKey(zoneId));
    }

    private String getTickStateKey(final long zoneId, final long tick) {
        // omgstate:zone:1:tick:2:tick-state
        return "%s:zone:%d:tick:%d:tick-state".formatted(prefix, zoneId, tick);
    }

    private String getZoneStateKey(final long zoneId) {
        // omgstate:zone:1:zone-state
        return "%s:zone:%d:zone-state".formatted(prefix, zoneId);
    }

    private String getZoneTickKey(final long zoneId) {
        // omgstate:zone:1:zone-tick
        return "%s:zone:%d:zone-tick".formatted(prefix, zoneId);
    }
}
