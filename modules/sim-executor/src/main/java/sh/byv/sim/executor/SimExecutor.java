package sh.byv.sim.executor;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class SimExecutor {

    public void execute(final long simId, final long tick) {
        log.info("Executing sim {} at tick {}", simId, tick);
    }
}
