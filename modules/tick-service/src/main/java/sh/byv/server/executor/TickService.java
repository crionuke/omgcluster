package sh.byv.server.executor;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.control.ActivateRequestContext;
import lombok.extern.slf4j.Slf4j;
import sh.byv.cache.service.CacheService;
import sh.byv.cache.service.CachedZone;
import sh.byv.mdc.id.WithMdcId;
import sh.byv.server.entity.ServerConfig;
import sh.byv.signal.service.SignalBody;
import sh.byv.signal.service.SignalEnvelope;
import sh.byv.signal.service.SignalService;
import sh.byv.signal.service.SignalType;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@ApplicationScoped
public class TickService {

    final ScheduledExecutorService scheduler;
    final SignalService signals;
    final CacheService cache;
    final TickService self;

    final String serverName;
    final long interval;

    public TickService(final CacheService cache,
                       final ServerConfig serverConfig,
                       final TickConfig tickConfig,
                       final SignalService signals,
                       final TickService self) {
        this.cache = cache;
        this.signals = signals;
        this.self = self;

        serverName = serverConfig.name();
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
        final var zoneIds = cache.getServerZones(serverName).stream().map(CachedZone::zoneId).toList();
        if (zoneIds.isEmpty()) {
            return;
        }

        final var envelope = new ArrayList<SignalBody>(zoneIds.size());

        zoneIds.forEach(zoneId -> {
            final var tick = cache.incrZoneTick(zoneId);
            final var signal = new SignalBody(SignalType.TICK, new SignalBody.TickSignal(zoneId, tick));

            envelope.add(signal);
        });

        signals.publish(new SignalEnvelope(envelope));
    }
}
