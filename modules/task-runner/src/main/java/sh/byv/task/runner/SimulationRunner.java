package sh.byv.task.runner;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.cache.service.CacheService;
import sh.byv.task.service.TaskItem;
import sh.byv.task.service.TaskResult;
import sh.byv.task.service.TaskRunner;
import sh.byv.task.service.TaskType;

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
        final var simulation = task.getSimulation();
        if (simulation == null) {
            throw new IllegalArgumentException("Task simulation field is null");
        }

        final var zoneId = simulation.getZoneId();
        final var tickId = simulation.getTickId();
        final var currentTick = cache.currentTick(zoneId);
        if (currentTick > tickId) {
            log.warn("Discarding stale task for zone {} tick {} (current: {})", zoneId, tickId, currentTick);
            return null;
        }

        log.debug("Simulated tick {} for zone {}", tickId, zoneId);

        final var result = new TaskResult();
        result.setSimulation(new TaskResult.Simulation());
        return result;
    }
}
