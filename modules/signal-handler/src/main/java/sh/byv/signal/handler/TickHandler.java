package sh.byv.signal.handler;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import sh.byv.signal.service.SignalBody;
import sh.byv.signal.service.SignalHandler;
import sh.byv.signal.service.SignalType;

@Slf4j
@ApplicationScoped
public class TickHandler implements SignalHandler {

    @Override
    public SignalType getType() {
        return SignalType.TICK;
    }

    @Override
    public void execute(final SignalBody signal) {
        log.info("Tick signal received: zoneId={}, tickId={}", signal.getTick().getZoneId(),
                signal.getTick().getTickId());
    }
}
