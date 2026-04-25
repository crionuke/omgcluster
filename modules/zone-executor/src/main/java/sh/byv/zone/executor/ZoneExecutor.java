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

    final RuntimeService runtime;
    final CacheService cache;

    public void execute(final long zoneId, final long tick) {
        log.trace("Executing zone {} tick {}", zoneId, tick);

        final var latestExecutedTick = cache.getZoneLatestExecutedTick(zoneId);
        if (latestExecutedTick == null) {
            log.warn("Zone {} tick {}: no executed tick found", zoneId, tick);
            return;
        }

        final var latestZoneState = cache.getZoneTickState(zoneId, latestExecutedTick);
        if (latestZoneState == null) {
            log.error("Zone {} tick {}: no zone state found", zoneId, tick);
            return;
        }

        final var zoneSims = cache.getZoneSims(zoneId);
        final var prevSimStates = zoneSims.stream()
                .map(CachedZoneSim::simId)
                .map(simId -> cache.getSimState(simId, latestExecutedTick))
                .filter(Objects::nonNull)
                .toList();

        if (prevSimStates.size() < zoneSims.size()) {
            log.warn("Zone {} tick {}: sim states {}/{}", zoneId, tick, prevSimStates.size(), zoneSims.size());
            finishExecution(zoneId, tick, latestZoneState);
            return;
        }

        final Object nextZoneState = runtime.computeZone(latestZoneState, prevSimStates, tick);
        if (nextZoneState == null) {
            log.warn("Zone {} tick {}: no state produced", zoneId, tick);
            finishExecution(zoneId, tick, latestZoneState);
            return;
        }

        finishExecution(zoneId, tick, nextZoneState);
    }

    void finishExecution(final long zoneId, final long tick, final Object zoneState) {
        cache.setZoneTickState(zoneId, tick, zoneState);
        cache.addZoneExecutedTick(zoneId, tick);
    }
}
