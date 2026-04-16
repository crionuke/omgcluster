package sh.byv.sim.executor;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.state.entity.StateBody;
import sh.byv.task.executor.TaskExecutor;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class SimExecutor {

    final TaskExecutor taskExecutor;

    public void execute(final StateBody.SimState sim, final Long tick) {
        log.info("Executing sim {} at tick {}", sim.id(), tick);

        taskExecutor.execute(() -> {

        });
    }
}
