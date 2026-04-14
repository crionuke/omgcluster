package sh.byv.job.worker;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.cache.CacheService;
import sh.byv.job.JobWorker;
import sh.byv.job.JobType;
import sh.byv.sim.SimService;
import sh.byv.state.StateService;
import sh.byv.task.TaskItem;
import sh.byv.task.TaskService;
import sh.byv.task.TaskType;
import sh.byv.zone.ZoneService;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class ZoneWorker implements JobWorker {

    final StateService states;
    final CacheService cache;
    final TaskService tasks;
    final ZoneService zones;
    final SimService sims;

    @Override
    public JobType getType() {
        return JobType.ZONE;
    }

    @Override
    public void execute() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public void execute(final Long zoneId) {
        final var tickId = cache.nextTick(zoneId);
        log.info("Tick ID: {}", tickId);

        final var resultKey = "zone:%d:tick:%d:results".formatted(zoneId, tickId);

        final var body = states.getThisState();
        final var zone = body.getZones().get(zoneId);
        if (zone == null) {
            return;
        }

        zone.getSims().values().forEach(sim -> {
            final var task = new TaskItem();
            task.setType(TaskType.SIMULATION);
            task.setResultKey(new TaskItem.MapField(resultKey, "sim"));
            task.setSimulation(new TaskItem.Simulation(zoneId, tickId, sim.getName()));

            tasks.pushTask(task);
        });

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }

        final var results = tasks.getResult(resultKey);
    }
}
