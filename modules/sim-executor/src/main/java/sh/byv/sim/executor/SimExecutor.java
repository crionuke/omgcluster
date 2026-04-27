package sh.byv.sim.executor;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.cache.service.CacheService;
import sh.byv.runtime.service.RuntimeService;
import sh.byv.runtime.service.SimulationContext;
import sh.byv.sim.entity.SimStatus;
import sh.byv.state.service.StateService;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class SimExecutor {

    final SimulationContext.Builder builder;
    final RuntimeService runtime;
    final CacheService cache;
    final StateService state;

    public void execute(final long simId, final long tick) {
        log.trace("Executing sim {} tick {}", simId, tick);

        final var sim = cache.getSimEntity(simId);
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
        final var state = this.state.getZoneState(zoneId, prevTick);
        if (state == null) {
            log.debug("Sim {} tick {} skipped: no prev zone state", simId, tick);
            return;
        }

        final var context = builder.build(tick, sim, state);
        final var result = runtime.simulate(context);
        if (result == null) {
            log.warn("Sim {} tick {}: no state produced", simId, tick);
            return;
        }

        this.state.setSimState(simId, tick, result);
    }
}
