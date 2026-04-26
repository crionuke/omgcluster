package sh.byv.server.executor;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.control.ActivateRequestContext;
import lombok.extern.slf4j.Slf4j;
import sh.byv.cache.service.CacheService;
import sh.byv.cache.service.CachedServerZone;
import sh.byv.mdc.id.WithMdcId;
import sh.byv.server.entity.ServerService;
import sh.byv.signal.service.SignalBody;
import sh.byv.signal.service.SignalEnvelope;
import sh.byv.signal.service.SignalService;
import sh.byv.signal.service.SignalType;
import sh.byv.state.service.StateService;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@ApplicationScoped
public class TickService {

    final ScheduledExecutorService scheduler;
    final SignalService signals;
    final ServerService servers;
    final CacheService cache;
    final StateService state;
    final TickService self;

    final long interval;

    public TickService(final CacheService cache,
                       final StateService state,
                       final ServerService servers,
                       final TickConfig tickConfig,
                       final SignalService signals,
                       final TickService self) {
        this.cache = cache;
        this.state = state;
        this.servers = servers;
        this.signals = signals;
        this.self = self;

        interval = tickConfig.interval();

        scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    public void start() {
        scheduler.scheduleAtFixedRate(self::tick, 0, interval, TimeUnit.MILLISECONDS);
    }

    public void shutdown() {
        scheduler.shutdown();
    }

    @WithMdcId
    @ActivateRequestContext
    void tick() {
        final var serverId = servers.getThisServerId();
        final var zoneIds = cache.getServerZones(serverId).stream().map(CachedServerZone::zoneId).toList();
        if (zoneIds.isEmpty()) {
            return;
        }

        final var envelope = new ArrayList<SignalBody>(zoneIds.size());

        zoneIds.forEach(zoneId -> {
            final var tick = state.incrZoneTick(zoneId);
            final var signal = new SignalBody(SignalType.TICK, new SignalBody.TickSignal(zoneId, tick));

            envelope.add(signal);
        });

        signals.publish(new SignalEnvelope(envelope));
    }
}
