package sh.byv.zone.executor;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import sh.byv.state.entity.StateBody;

@Slf4j
@ApplicationScoped
public class ZoneExecutor {

    public void execute(final StateBody.ZoneState zone, final Long tick) {
        log.info("Executing zone {} at tick {}", zone.id(), tick);
    }
}
