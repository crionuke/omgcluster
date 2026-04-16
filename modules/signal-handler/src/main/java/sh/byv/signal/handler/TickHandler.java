package sh.byv.signal.handler;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.signal.service.SignalBody;
import sh.byv.signal.service.SignalHandler;
import sh.byv.signal.service.SignalType;
import sh.byv.sim.executor.SimExecutor;
import sh.byv.state.entity.StateCache;
import sh.byv.zone.executor.ZoneExecutor;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class TickHandler implements SignalHandler {

    final ZoneExecutor zones;
    final StateCache states;
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

        states.getServerState().ifPresent(state -> {
            final var zone = state.getZones().get(zoneId);
            if (zone != null) {
                zones.execute(zone, tick);
            }

            state.getSims().values().stream()
                    .filter(sim -> sim.zoneId() == zoneId)
                    .forEach(sim -> sims.execute(sim, tick));
        });
    }
}
