package sh.byv.server.executor;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import sh.byv.signal.service.SignalBody;
import sh.byv.signal.service.SignalEnvelope;
import sh.byv.signal.service.SignalService;
import sh.byv.signal.service.SignalType;
import sh.byv.state.entity.StateBody;
import sh.byv.state.entity.StateCache;
import sh.byv.zone.entity.ZoneCache;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@ApplicationScoped
public class TickService {


    final ScheduledExecutorService scheduler;
    final SignalService signals;
    final TickCache servers;
    final StateCache states;
    final ZoneCache zones;

    public TickService(final TickCache servers,
                       final StateCache states,
                       final ZoneCache zones,
                       final SignalService signals) {
        this.servers = servers;
        this.states = states;
        this.zones = zones;
        this.signals = signals;

        scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    public void start() {
        scheduler.scheduleAtFixedRate(() -> states.getServerState()
                .ifPresent(this::tick), 0, 1000, TimeUnit.MILLISECONDS);
    }

    public void shutdown() {
        scheduler.shutdown();
    }

    void tick(final StateBody state) {
        if (state.getZones().isEmpty()) {
            return;
        }

        final var envelope = new ArrayList<SignalBody>(state.getZones().size());

        state.getZones().forEach((zoneId, _) -> {
            final var tick = zones.incrZoneTick(zoneId);
            final var signal = new SignalBody(SignalType.TICK, new SignalBody.TickSignal(zoneId, tick));

            envelope.add(signal);
        });

        this.signals.publish(new SignalEnvelope(envelope));
    }
}
