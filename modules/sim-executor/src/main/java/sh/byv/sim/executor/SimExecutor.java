package sh.byv.sim.executor;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import sh.byv.state.entity.StateBody;

@Slf4j
@ApplicationScoped
public class SimExecutor {

    public void execute(final StateBody.SimState sim, final Long tick) {
        log.info("Executing sim {} at tick {}", sim.id(), tick);
    }
}
