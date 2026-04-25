package sh.byv.signal.handler;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.cache.service.CacheService;
import sh.byv.cache.service.CachedServerSim;
import sh.byv.cache.service.CachedServerZone;
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
            log.error("Received signal with null body");
            return;
        }

        final var zoneId = tickSignal.getZoneId();
        final var tick = tickSignal.getTick();

        log.debug("Handling zone {} tick {}", zoneId, tick);

        final var serverId = servers.getThisServerId();

        // Execute sims
        cache.getServerSims(serverId).stream()
                .filter(sim -> sim.zoneId() == zoneId)
                .map(CachedServerSim::simId)
                .forEach(simId -> taskExecutor.execute(() -> sims.execute(simId, tick)));

        // Execute zone
        cache.getServerZones(serverId).stream()
                .map(CachedServerZone::zoneId)
                .filter(id -> id == zoneId)
                .findAny()
                .ifPresent(id -> taskExecutor.execute(() -> zones.execute(id, tick)));
    }
}
