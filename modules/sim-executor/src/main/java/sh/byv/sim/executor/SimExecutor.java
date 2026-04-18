package sh.byv.sim.executor;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.task.executor.TaskExecutor;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class SimExecutor {

    final TaskExecutor taskExecutor;

    public void execute(final long simId, final long tick) {
        taskExecutor.execute(() -> {
            log.info("Executing sim {} at tick {}", simId, tick);

        });
    }
}
