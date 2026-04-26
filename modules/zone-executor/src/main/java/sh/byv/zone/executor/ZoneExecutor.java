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

        final var prevTick = state.getStateTick(zoneId);
        final var prevState = state.getZoneState(zoneId, prevTick);

        final var zoneSims = cache.getZoneSims(zoneId);
        final var prevSimStates = zoneSims.stream()
                .map(CachedZoneSim::simId)
                .map(simId -> state.getSimState(simId, prevTick))
                .filter(Objects::nonNull)
                .toList();

        if (prevSimStates.size() < zoneSims.size()) {
            log.warn("Zone {} tick {} skipped: sim states {}/{}", zoneId, tick, prevSimStates.size(), zoneSims.size());
            state.setZoneState(zoneId, tick, prevState);
            return;
        }

        final Object nextZoneState = runtime.computeZone(prevState, prevSimStates, tick);
        if (nextZoneState == null) {
            log.warn("Zone {} tick {} skipped: no state produced", zoneId, tick);
            state.setZoneState(zoneId, tick, prevState);
            return;
        }

        final var zoneTick = state.getZoneTick(zoneId);
        if (zoneTick > tick) {
            log.warn("Zone {} tick {} skipped: stale tick", zoneId, tick);
            return;
        }

        state.setZoneState(zoneId, tick, nextZoneState);
    }
}
