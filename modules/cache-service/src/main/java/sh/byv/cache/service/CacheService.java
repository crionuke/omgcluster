package sh.byv.cache.service;

import io.quarkus.cache.CacheInvalidate;
import io.quarkus.cache.CacheResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.TransactionPhase;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.layer.entity.LayerModel;
import sh.byv.layer.entity.LayerService;
import sh.byv.server.entity.ServerRelEntity;
import sh.byv.server.entity.ServerRelService;
import sh.byv.server.entity.ServerRelType;
import sh.byv.server.entity.ServerService;
import sh.byv.sim.entity.SimModel;
import sh.byv.sim.entity.SimService;
import sh.byv.world.entity.WorldModel;
import sh.byv.world.entity.WorldService;
import sh.byv.zone.entity.ZoneModel;
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
    final LayerService layers;
    final WorldService worlds;

    @CacheResult(cacheName = "server-zones", keyGenerator = ServerCacheKeyGenerator.class)
    public List<ZoneModel> getServerZones(final long serverId) {
        log.info("Cache miss for server zones: {}", serverId);

        final var server = servers.getByIdRequired(serverId);
        return rels.getByServerAndType(server, ServerRelType.ZONE).stream()
                .map(ServerRelEntity::getEntityId)
                .map(zones::getByIdRequired)
                .map(zone -> zone.toModel())
                .toList();
    }

    @CacheInvalidate(cacheName = "server-zones", keyGenerator = ServerCacheKeyGenerator.class)
    public void invalidateServerZones(final long serverId) {
        log.info("Invalidated server zones cache for {}", serverId);
    }

    @CacheResult(cacheName = "server-sims", keyGenerator = ServerCacheKeyGenerator.class)
    public List<SimModel> getServerSims(final long serverId) {
        log.info("Cache miss for server sims: {}", serverId);

        final var server = servers.getByIdRequired(serverId);
        final var simIds = rels.getByServerAndType(server, ServerRelType.SIM).stream()
                .map(ServerRelEntity::getEntityId)
                .toList();

        if (simIds.isEmpty()) {
            return List.of();
        }

        return sims.getByIds(simIds).stream()
                .map(sim -> sim.toModel())
                .toList();
    }

    @CacheInvalidate(cacheName = "server-sims", keyGenerator = ServerCacheKeyGenerator.class)
    public void invalidateServerSims(final long serverId) {
        log.info("Invalidated server sims cache for {}", serverId);
    }

    @CacheResult(cacheName = "zone-sims", keyGenerator = ZoneCacheKeyGenerator.class)
    public List<SimModel> getZoneSims(final long zoneId) {
        log.info("Cache miss for zone sims: {}", zoneId);

        final var zone = zones.getByIdRequired(zoneId);
        return sims.getByZone(zone).stream()
                .map(sim -> sim.toModel())
                .toList();
    }

    @CacheInvalidate(cacheName = "zone-sims", keyGenerator = ZoneCacheKeyGenerator.class)
    public void invalidateZoneSims(final long zoneId) {
        log.info("Invalidated zone sims cache for {}", zoneId);
    }

    @CacheResult(cacheName = "sim-entity", keyGenerator = SimCacheKeyGenerator.class)
    public SimModel getSimEntity(final long simId) {
        log.info("Cache miss for sim: {}", simId);

        return sims.getByIdOptional(simId)
                .map(sim -> sim.toModel())
                .orElse(null);
    }

    @CacheInvalidate(cacheName = "sim-entity", keyGenerator = SimCacheKeyGenerator.class)
    public void invalidateSimEntity(final long simId) {
        log.info("Invalidated sim cache for {}", simId);
    }

    @CacheResult(cacheName = "zone-entity", keyGenerator = ZoneCacheKeyGenerator.class)
    public ZoneModel getZoneEntity(final long zoneId) {
        log.info("Cache miss for zone: {}", zoneId);

        return zones.getByIdRequired(zoneId).toModel();
    }

    @CacheInvalidate(cacheName = "zone-entity", keyGenerator = ZoneCacheKeyGenerator.class)
    public void invalidateZoneEntity(final long zoneId) {
        log.info("Invalidated zone cache for {}", zoneId);
    }

    @CacheResult(cacheName = "layer-entity", keyGenerator = LayerCacheKeyGenerator.class)
    public LayerModel getLayerEntity(final long layerId) {
        log.info("Cache miss for layer: {}", layerId);

        return layers.getByIdRequired(layerId).toModel();
    }

    @CacheInvalidate(cacheName = "layer-entity", keyGenerator = LayerCacheKeyGenerator.class)
    public void invalidateLayerEntity(final long layerId) {
        log.info("Invalidated layer cache for {}", layerId);
    }

    @CacheResult(cacheName = "world-entity", keyGenerator = WorldCacheKeyGenerator.class)
    public WorldModel getWorldEntity(final long worldId) {
        log.info("Cache miss for world: {}", worldId);

        return worlds.getByIdRequired(worldId).toModel();
    }

    @CacheInvalidate(cacheName = "world-entity", keyGenerator = WorldCacheKeyGenerator.class)
    public void invalidateWorldEntity(final long worldId) {
        log.info("Invalidated world cache for {}", worldId);
    }

    void onCacheInvalidation(@Observes(during = TransactionPhase.AFTER_SUCCESS) final CacheEvent event) {
        final var entityId = event.entityId();

        switch (event.type()) {
            case SERVER_ZONES -> invalidateServerZones(entityId);
            case SERVER_SIMS -> invalidateServerSims(entityId);
            case ZONE_SIMS -> invalidateZoneSims(entityId);
            case SIM_ENTITY -> invalidateSimEntity(entityId);
            case ZONE_ENTITY -> invalidateZoneEntity(entityId);
            case LAYER_ENTITY -> invalidateLayerEntity(entityId);
            case WORLD_ENTITY -> invalidateWorldEntity(entityId);
        }
    }
}
