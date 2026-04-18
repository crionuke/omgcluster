package sh.byv.signal.handler;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import sh.byv.cache.service.CacheService;
import sh.byv.server.entity.ServerConfig;
import sh.byv.signal.service.SignalBody;
import sh.byv.signal.service.SignalHandler;
import sh.byv.signal.service.SignalType;
import sh.byv.sim.executor.SimExecutor;
import sh.byv.zone.executor.ZoneExecutor;

@Slf4j
@ApplicationScoped
public class TickHandler implements SignalHandler {

    final CacheService cache;
    final ZoneExecutor zones;
    final SimExecutor sims;

    final String serverName;

    public TickHandler(final CacheService cache,
                       final ServerConfig config,
                       final ZoneExecutor zones,
                       final SimExecutor sims) {
        this.cache = cache;
        this.zones = zones;
        this.sims = sims;

        serverName = config.name();
    }

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

        zones.execute(zoneId, tick);
        cache.getServerSimIds(serverName).forEach(simId -> sims.execute(simId, tick));
    }
}
