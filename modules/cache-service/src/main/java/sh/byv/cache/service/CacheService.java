package sh.byv.cache.service;

import io.quarkus.cache.CacheInvalidate;
import io.quarkus.cache.CacheResult;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.value.ValueCommands;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import sh.byv.server.entity.ServerRelEntity;
import sh.byv.server.entity.ServerRelService;
import sh.byv.server.entity.ServerRelType;
import sh.byv.server.entity.ServerService;
import sh.byv.sim.entity.SimService;

import java.util.List;

@Slf4j
@ApplicationScoped
public class CacheService {

    final ServerRelService rels;
    final ServerService servers;
    final SimService sims;

    final ValueCommands<String, Long> longCommands;
    final String prefix;

    public CacheService(final ServerRelService rels,
                        final ServerService servers,
                        final SimService sims,
                        final CacheConfig config,
                        final RedisDataSource redis) {
        this.rels = rels;
        this.servers = servers;
        this.sims = sims;

        longCommands = redis.value(Long.class);
        prefix = config.prefix();
    }

    private String getZoneTickKey(final long zoneId) {
        return "%s:zone:%d:tick".formatted(prefix, zoneId);
    }

    @CacheResult(cacheName = "rel-zones", keyGenerator = ServerCacheKeyGenerator.class)
    public List<RelZone> getServerZones(final long serverId) {
        log.info("Cache miss for server zones: {}", serverId);

        final var server = servers.getByIdRequired(serverId);
        return rels.getByServerAndType(server, ServerRelType.ZONE).stream()
                .map(ServerRelEntity::getEntityId)
                .map(RelZone::new)
                .toList();
    }

    @CacheResult(cacheName = "rel-sims", keyGenerator = ServerCacheKeyGenerator.class)
    public List<RelSim> getServerSims(final long serverId) {
        log.info("Cache miss for server sims: {}", serverId);

        final var server = servers.getByIdRequired(serverId);
        final var simIds = rels.getByServerAndType(server, ServerRelType.SIM).stream()
                .map(ServerRelEntity::getEntityId)
                .toList();

        if (simIds.isEmpty()) {
            return List.of();
        }

        return sims.getByIds(simIds).stream()
                .map(sim -> new RelSim(sim.getZone().getId(), sim.getId()))
                .toList();
    }

    @CacheInvalidate(cacheName = "rel-zones", keyGenerator = ServerCacheKeyGenerator.class)
    public void invalidateServerZones(final long serverId) {
        log.info("Invalidated server zone ids cache for {}", serverId);
    }

    @CacheInvalidate(cacheName = "rel-sims", keyGenerator = ServerCacheKeyGenerator.class)
    public void invalidateServerSims(final long serverId) {
        log.info("Invalidated server sim ids cache for {}", serverId);
    }

    public long getZoneTick(final long zoneId) {
        final var value = longCommands.get(getZoneTickKey(zoneId));
        return value != null ? value : 0L;
    }

    public long incrZoneTick(final long zoneId) {
        return longCommands.incr(getZoneTickKey(zoneId));
    }
}
