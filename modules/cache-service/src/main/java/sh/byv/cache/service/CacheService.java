package sh.byv.cache.service;

import io.quarkus.cache.CacheInvalidate;
import io.quarkus.cache.CacheResult;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.sortedset.SortedSetCommands;
import io.quarkus.redis.datasource.value.ValueCommands;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.TransactionPhase;
import lombok.extern.slf4j.Slf4j;
import sh.byv.server.entity.ServerRelEntity;
import sh.byv.server.entity.ServerRelService;
import sh.byv.server.entity.ServerRelType;
import sh.byv.server.entity.ServerService;
import sh.byv.sim.entity.SimService;
import sh.byv.zone.entity.ZoneService;

import java.util.List;

@Slf4j
@ApplicationScoped
public class CacheService {

    final ServerRelService rels;
    final ServerService servers;
    final CacheConfig config;
    final ZoneService zones;
    final SimService sims;

    final ValueCommands<String, Object> objects;
    final SortedSetCommands<String, Long> ssets;
    final ValueCommands<String, Long> longs;


    final String prefix;

    public CacheService(final ServerRelService rels,
                        final ServerService servers,
                        final SimService sims,
                        final ZoneService zones,
                        final CacheConfig config,
                        final RedisDataSource redis) {
        this.rels = rels;
        this.servers = servers;
        this.sims = sims;
        this.zones = zones;
        this.config = config;

        objects = redis.value(Object.class);
        ssets = redis.sortedSet(Long.class);
        longs = redis.value(Long.class);

        prefix = config.prefix();
    }

    @CacheResult(cacheName = "server-zones", keyGenerator = ServerCacheKeyGenerator.class)
    public List<CachedServerZone> getServerZones(final long serverId) {
        log.info("Cache miss for server zones: {}", serverId);

        final var server = servers.getByIdRequired(serverId);
        return rels.getByServerAndType(server, ServerRelType.ZONE).stream()
                .map(ServerRelEntity::getEntityId)
                .map(CachedServerZone::new)
                .toList();
    }

    @CacheInvalidate(cacheName = "server-zones", keyGenerator = ServerCacheKeyGenerator.class)
    public void invalidateServerZones(final long serverId) {
        log.info("Invalidated server zones cache for {}", serverId);
    }

    @CacheResult(cacheName = "server-sims", keyGenerator = ServerCacheKeyGenerator.class)
    public List<CachedServerSim> getServerSims(final long serverId) {
        log.info("Cache miss for server sims: {}", serverId);

        final var server = servers.getByIdRequired(serverId);
        final var simIds = rels.getByServerAndType(server, ServerRelType.SIM).stream()
                .map(ServerRelEntity::getEntityId)
                .toList();

        if (simIds.isEmpty()) {
            return List.of();
        }

        return sims.getByIds(simIds).stream()
                .map(sim -> new CachedServerSim(sim.getZone().getId(), sim.getId()))
                .toList();
    }

    @CacheInvalidate(cacheName = "server-sims", keyGenerator = ServerCacheKeyGenerator.class)
    public void invalidateServerSims(final long serverId) {
        log.info("Invalidated server sims cache for {}", serverId);
    }

    @CacheResult(cacheName = "zone-sims", keyGenerator = ZoneCacheKeyGenerator.class)
    public List<CachedZoneSim> getZoneSims(final long zoneId) {
        log.info("Cache miss for zone sims: {}", zoneId);

        final var zone = zones.getByIdRequired(zoneId);
        return sims.getByZone(zone).stream()
                .map(sim -> new CachedZoneSim(sim.getId(), sim.getName()))
                .toList();
    }

    @CacheInvalidate(cacheName = "zone-sims", keyGenerator = ZoneCacheKeyGenerator.class)
    public void invalidateZoneSims(final long zoneId) {
        log.info("Invalidated zone sims cache for {}", zoneId);
    }

    @CacheResult(cacheName = "cached-sim", keyGenerator = SimCacheKeyGenerator.class)
    public CachedSim getCachedSim(final long simId) {
        log.info("Cache miss for cached sim: {}", simId);

        return sims.getByIdOptional(simId)
                .map(CachedSim::from)
                .orElse(null);
    }

    @CacheInvalidate(cacheName = "cached-sim", keyGenerator = SimCacheKeyGenerator.class)
    public void invalidateCachedSim(final long simId) {
        log.info("Invalidated cached sim cache for {}", simId);
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

    public void setZoneTickState(final long zoneId, final long tick, final Object state) {
        final var ttl = config.zoneState().ttl().getSeconds();
        objects.setex(getZoneTickStateKey(zoneId, tick), ttl, state);
    }

    public Object getZoneTickState(final long zoneId, final long tick) {
        return objects.get(getZoneTickStateKey(zoneId, tick));
    }

    private String getZoneTickStateKey(final long zoneId, final long tick) {
        // omgc:zone:1:tick:2:zone-state
        return "%s:zone:%d:tick:%d:zone-state".formatted(prefix, zoneId, tick);
    }

    public long getZoneTick(final long zoneId) {
        final var value = longs.get(getZoneTickKey(zoneId));
        return value != null ? value : 0L;
    }

    public long incrZoneTick(final long zoneId) {
        return longs.incr(getZoneTickKey(zoneId));
    }

    private String getZoneTickKey(final long zoneId) {
        // omgc:zone:1:tick
        return "%s:zone:%d:tick".formatted(prefix, zoneId);
    }

    public void addZoneExecutedTick(final long zoneId, final long tick) {
        ssets.zadd(getZoneExecutedTicksKey(zoneId), tick, tick);
    }

    public Long getZoneLatestExecutedTick(final long zoneId) {
        final var values = ssets.zrange(getZoneExecutedTicksKey(zoneId), -1, -1);
        if (values.isEmpty()) {
            return null;
        } else {
            return values.getFirst();
        }
    }

    private String getZoneExecutedTicksKey(final long zoneId) {
        // omgc:zone:1:executed-ticks
        return "%s:zone:%d:executed-ticks".formatted(prefix, zoneId);
    }

    void onCacheInvalidation(@Observes(during = TransactionPhase.AFTER_SUCCESS) final CacheEvent event) {
        final var entityId = event.entityId();

        switch (event.type()) {
            case SERVER_ZONES -> invalidateServerZones(entityId);
            case SERVER_SIMS -> invalidateServerSims(entityId);
            case ZONE_SIMS -> invalidateZoneSims(entityId);
            case CACHED_SIM -> invalidateCachedSim(entityId);
        }
    }
}
