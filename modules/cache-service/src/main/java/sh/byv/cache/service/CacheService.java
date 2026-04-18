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
    public List<CachedZone> getServerZones(final String serverName) {
        log.info("Cache miss for server zones: {}", serverName);

        final var server = servers.getByNameRequired(serverName);
        return rels.getByServerAndType(server, ServerRelType.ZONE).stream()
                .map(ServerRelEntity::getEntityId)
                .map(CachedZone::new)
                .toList();
    }

    @CacheResult(cacheName = "rel-sims", keyGenerator = ServerCacheKeyGenerator.class)
    public List<CachedSim> getServerSims(final String serverName) {
        log.info("Cache miss for server sims: {}", serverName);

        final var server = servers.getByNameRequired(serverName);
        final var simIds = rels.getByServerAndType(server, ServerRelType.SIM).stream()
                .map(ServerRelEntity::getEntityId)
                .toList();

        if (simIds.isEmpty()) {
            return List.of();
        }

        return sims.getByIds(simIds).stream()
                .map(sim -> new CachedSim(sim.getZone().getId(), sim.getId()))
                .toList();
    }

    @CacheInvalidate(cacheName = "rel-zones", keyGenerator = ServerCacheKeyGenerator.class)
    public void invalidateServerZones(final String serverName) {
        log.info("Invalidated server zone ids cache for {}", serverName);
    }

    @CacheInvalidate(cacheName = "rel-sims", keyGenerator = ServerCacheKeyGenerator.class)
    public void invalidateServerSims(final String serverName) {
        log.info("Invalidated server sim ids cache for {}", serverName);
    }

    public long getZoneTick(final long zoneId) {
        final var value = longCommands.get(getZoneTickKey(zoneId));
        return value != null ? value : 0L;
    }

    public long incrZoneTick(final long zoneId) {
        return longCommands.incr(getZoneTickKey(zoneId));
    }
}
