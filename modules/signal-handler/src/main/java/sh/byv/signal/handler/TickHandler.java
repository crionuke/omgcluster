package sh.byv.signal.handler;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.cache.service.CacheService;
import sh.byv.cache.service.RelSim;
import sh.byv.cache.service.RelZone;
import sh.byv.server.entity.ServerService;
import sh.byv.signal.service.SignalBody;
import sh.byv.signal.service.SignalHandler;
import sh.byv.signal.service.SignalType;
import sh.byv.sim.executor.SimExecutor;
import sh.byv.task.executor.TaskExecutor;
import sh.byv.zone.executor.ZoneExecutor;

@Slf4j
@AllArgsConstructor
@ApplicationScoped
public class TickHandler implements SignalHandler {

    final CacheService cache;
    final ServerService servers;
    final TaskExecutor taskExecutor;
    final ZoneExecutor zones;
    final SimExecutor sims;

    @Override
    public SignalType getType() {
        return SignalType.TICK;
    }

    @Override
    public void execute(final SignalBody signal) {
        final var tickSignal = signal.getTick();
        if (tickSignal == null) {
            return;
        }

        final var tick = tickSignal.getTick();
        final var zoneId = tickSignal.getZoneId();

        final var serverId = servers.getThisServerId();

        // Execute zone
        cache.getServerZones(serverId).stream()
                .map(RelZone::zoneId)
                .filter(id -> id == zoneId)
                .findAny()
                .ifPresent(id -> taskExecutor.execute(() -> zones.execute(id, tick)));

        // Execute sims
        cache.getServerSims(serverId).stream()
                .filter(sim -> sim.zoneId() == zoneId)
                .map(RelSim::simId)
                .forEach(simId -> taskExecutor.execute(() -> sims.execute(simId, tick)));
    }
}
