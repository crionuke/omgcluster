package sh.byv.zone.executor;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.cache.service.CacheService;
import sh.byv.cache.service.CachedZoneSim;
import sh.byv.runtime.service.RuntimeService;
import sh.byv.state.service.StateService;

import java.util.Objects;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class ZoneExecutor {

    final RuntimeService runtime;
    final CacheService cache;
    final StateService state;

    public void execute(final long zoneId, final long tick) {
        log.trace("Executing zone {} tick {}", zoneId, tick);

        final var prevTick = state.getLatestExecutedTick(zoneId);

        final var prevZoneState = state.getZoneState(zoneId, prevTick);
        if (prevZoneState == null) {
            log.error("Zone {} tick {}: no zone state found", zoneId, tick);
            return;
        }

        final var zoneSims = cache.getZoneSims(zoneId);
        final var prevSimStates = zoneSims.stream()
                .map(CachedZoneSim::simId)
                .map(simId -> state.getSimState(simId, prevTick))
                .filter(Objects::nonNull)
                .toList();

        if (prevSimStates.size() < zoneSims.size()) {
            log.warn("Zone {} tick {}: sim states {}/{}", zoneId, tick, prevSimStates.size(), zoneSims.size());
            state.setZoneState(zoneId, tick, prevZoneState);
            state.addExecutedTick(zoneId, tick);
            return;
        }

        final Object nextZoneState = runtime.computeZone(prevZoneState, prevSimStates, tick);
        if (nextZoneState == null) {
            log.warn("Zone {} tick {}: no state produced", zoneId, tick);
            state.setZoneState(zoneId, tick, prevZoneState);
            state.addExecutedTick(zoneId, tick);
            return;
        }

        state.setZoneState(zoneId, tick, nextZoneState);
        state.addExecutedTick(zoneId, tick);
    }
}
