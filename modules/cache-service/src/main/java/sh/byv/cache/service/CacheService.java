package sh.byv.cache.service;

import io.quarkus.cache.CacheInvalidate;
import io.quarkus.cache.CacheResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.TransactionPhase;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class CacheService {

    final ServerRelService rels;
    final ServerService servers;
    final ZoneService zones;
    final SimService sims;

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
