package sh.byv.sim.executor;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.cache.service.CacheService;
import sh.byv.runtime.service.RuntimeService;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class SimExecutor {

    final RuntimeService runtime;
    final CacheService cache;

    public void execute(final long simId, final long tick) {
        final var sim = cache.getCachedSim(simId);
        final var zoneState = cache.getZoneState(sim.zoneId());

        log.info("Executing sim {} ({}) at tick {}", sim.name(), simId, tick);

        final var simState = runtime.simulate(tick, sim.name(), zoneState);
        if (simState != null) {
            cache.setSimState(simId, tick, simState);
        }
    }
}
