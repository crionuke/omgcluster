package sh.byv.zone.executor;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.task.executor.TaskExecutor;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class ZoneExecutor {

    final TaskExecutor taskExecutor;

    public void execute(final long zoneId, final long tick) {
        taskExecutor.execute(() -> {
            log.info("Executing zone {} at tick {}", zoneId, tick);

        });
    }
}
