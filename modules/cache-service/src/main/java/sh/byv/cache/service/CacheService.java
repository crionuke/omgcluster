package sh.byv.cache.service;

import io.quarkus.cache.CacheInvalidate;
import io.quarkus.cache.CacheResult;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.value.ValueCommands;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import sh.byv.server.entity.ServerRelEntity;
import sh.byv.server.entity.ServerRelRepository;
import sh.byv.server.entity.ServerRelType;
import sh.byv.server.entity.ServerService;

import java.util.List;

@Slf4j
@ApplicationScoped
public class CacheService {

    private static String getZoneTickKey(final long zoneId) {
        return "omgc:zone:%d:tick".formatted(zoneId);
    }

    final ServerRelRepository rels;
    final ServerService servers;

    final ValueCommands<String, Long> longCommands;

    public CacheService(final ServerRelRepository rels,
                        final ServerService servers,
                        final RedisDataSource redis) {
        this.rels = rels;
        this.servers = servers;

        longCommands = redis.value(Long.class);
    }

    @CacheResult(cacheName = "server-zone-ids")
    public List<Long> getServerZoneIds(final String serverName) {
        log.info("Cache miss for server zone ids: {}", serverName);
        final var server = servers.getByNameRequired(serverName);
        return rels.findRelsByServerAndType(server, ServerRelType.ZONE).stream()
                .map(ServerRelEntity::getEntityId).toList();
    }

    @CacheResult(cacheName = "server-sim-ids")
    public List<Long> getServerSimIds(final String serverName) {
        log.info("Cache miss for server sim ids: {}", serverName);
        final var server = servers.getByNameRequired(serverName);
        return rels.findRelsByServerAndType(server, ServerRelType.SIM).stream()
                .map(ServerRelEntity::getEntityId).toList();
    }

    @CacheInvalidate(cacheName = "server-zone-ids")
    public void invalidateServerZoneIds(final String serverName) {
        log.info("Invalidated server zone ids cache for {}", serverName);
    }

    @CacheInvalidate(cacheName = "server-sim-ids")
    public void invalidateServerSimIds(final String serverName) {
        log.info("Invalidated server sim ids cache for {}", serverName);
    }

    public long getZoneTick(final long zoneId) {
        return longCommands.get(getZoneTickKey(zoneId));
    }

    public long incrZoneTick(final long zoneId) {
        return longCommands.incr(getZoneTickKey(zoneId));
    }
}
