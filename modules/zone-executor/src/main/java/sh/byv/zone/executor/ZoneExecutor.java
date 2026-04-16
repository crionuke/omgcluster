package sh.byv.zone.executor;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.state.entity.StateBody;
import sh.byv.task.executor.TaskExecutor;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class ZoneExecutor {

    final TaskExecutor taskExecutor;

    public void execute(final StateBody.ZoneState zone, final Long tick) {
        log.info("Executing zone {} at tick {}", zone.id(), tick);

        taskExecutor.execute(() -> {

        });
    }
}
