package sh.byv.runners;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.cache.CacheService;
import sh.byv.task.TaskItem;
import sh.byv.task.TaskResult;
import sh.byv.task.TaskRunner;
import sh.byv.task.TaskType;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class SimulationRunner implements TaskRunner {

    final CacheService cache;

    @Override
    public TaskType getType() {
        return TaskType.SIMULATION;
    }

    @Override
    public TaskResult execute(final TaskItem task) {
        final var zoneId = task.getSimulation().getZoneId();
        final var tickId = task.getSimulation().getTickId();
        final var currentTick = cache.currentTick(zoneId);
        if (currentTick > tickId) {
            log.warn("Discarding stale task for zone {} tick {} (current: {})", zoneId, tickId, currentTick);
            return null;
        }

        log.debug("Simulated tick {} for zone {}", tickId, zoneId);

        final var result = new TaskResult();
        return result;
    }
}
