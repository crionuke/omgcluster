package sh.byv.zone.executor;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.cache.service.CacheService;
import sh.byv.runtime.service.AggregationContext;
import sh.byv.sim.entity.SimModel;
import sh.byv.runtime.service.RuntimeService;
import sh.byv.sim.results.SimResults;
import sh.byv.zone.states.ZoneStates;

import java.util.Objects;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class ZoneExecutor {

    final AggregationContext.Builder builder;
    final RuntimeService runtime;
    final CacheService cache;
    final SimResults results;
    final ZoneStates states;

    public void execute(final long zoneId, final long tick) {
        log.trace("Executing zone {} tick {}", zoneId, tick);

        final var prevState = states.getZoneState(zoneId);
        final var prevTick = prevState.tick();

        final var zoneSims = cache.getZoneSims(zoneId);
        final var results = zoneSims.stream()
                .map(SimModel::id)
                .map(simId -> this.results.getSimResult(simId, prevTick))
                .filter(Objects::nonNull)
                .toList();

        if (results.size() < zoneSims.size()) {
            log.warn("Zone {} tick {} skipped: sim results {}/{}", zoneId, tick, results.size(), zoneSims.size());
            states.setTickState(zoneId, tick, prevState);
            return;
        }

        final var context = builder.build(tick, results, prevState);
        final Object nextState = runtime.aggregate(context);
        if (nextState == null) {
            log.warn("Zone {} tick {} skipped: no state produced", zoneId, tick);
            states.setTickState(zoneId, tick, prevState);
            return;
        }

        final var zoneTick = states.getZoneTick(zoneId);
        if (zoneTick > tick) {
            log.warn("Zone {} tick {} skipped: stale tick", zoneId, tick);
            states.setTickState(zoneId, tick, prevState);
            return;
        }

        states.setTickState(zoneId, tick, nextState);
    }
}
