package sh.byv.sim.executor;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.cache.service.CacheService;
import sh.byv.runtime.service.RuntimeService;
import sh.byv.sim.entity.SimStatus;
import sh.byv.state.service.StateService;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class SimExecutor {

    final RuntimeService runtime;
    final CacheService cache;
    final StateService state;

    public void execute(final long simId, final long tick) {
        log.trace("Executing sim {} tick {}", simId, tick);

        final var sim = cache.getCachedSim(simId);
        if (sim == null) {
            log.error("Sim {} tick {} skipped: sim not found", simId, tick);
            return;
        }

        if (sim.status() != SimStatus.ACTIVE) {
            log.debug("Sim {} tick {} skipped: status {}", simId, tick, sim.status());
            return;
        }

        final var zoneId = sim.zoneId();
        final var prevTick = tick - 1;
        final var zoneState = state.getZoneState(zoneId, prevTick);
        if (zoneState == null) {
            log.debug("Sim {} tick {} skipped: no prev zone state", simId, tick);
            return;
        }

        final var simState = runtime.simulateZone(tick, sim.name(), zoneState);
        if (simState == null) {
            log.warn("Sim {} tick {}: no state produced", simId, tick);
            return;
        }

        state.setSimState(simId, tick, simState);
    }
}
