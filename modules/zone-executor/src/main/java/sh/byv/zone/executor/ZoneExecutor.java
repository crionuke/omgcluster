package sh.byv.zone.executor;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class ZoneExecutor {

    public void execute(final long zoneId, final long tick) {
        log.info("Executing zone {} at tick {}", zoneId, tick);
    }
}
