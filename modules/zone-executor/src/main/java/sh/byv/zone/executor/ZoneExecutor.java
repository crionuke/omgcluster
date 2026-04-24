package sh.byv.zone.executor;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.cache.service.CacheService;
import sh.byv.cache.service.CachedZoneSim;
import sh.byv.runtime.service.RuntimeService;

import java.util.Objects;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class ZoneExecutor {

    final ZoneExecutorConfig config;
    final RuntimeService runtime;
    final CacheService cache;

    public void execute(final long zoneId, final long tick) {
        log.info("Executing zone {} at tick {}", zoneId, tick);

        try {
            Thread.sleep(config.delay());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Zone {} execution interrupted during delay", zoneId);
            return;
        }

        final var zoneSims = cache.getZoneSims(zoneId);

        final var simStates = zoneSims.stream()
                .map(CachedZoneSim::simId)
                .map(simId -> cache.getSimState(simId, tick))
                .filter(Objects::nonNull)
                .toList();

        final var prevZoneState = cache.getZoneState(zoneId);

        log.info("Aggregating {} sim states for zone {} at tick {}", simStates.size(), zoneId, tick);

        final var zoneState = runtime.aggregate(prevZoneState, simStates, tick);
        if (zoneState != null) {
            cache.setZoneState(zoneId, zoneState);
        }
    }
}
