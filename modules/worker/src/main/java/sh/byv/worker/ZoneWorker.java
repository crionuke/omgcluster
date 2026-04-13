package sh.byv.worker;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.cache.CacheService;
import sh.byv.job.JobWorker;
import sh.byv.job.JobType;
import sh.byv.sim.SimEntity;
import sh.byv.sim.SimService;
import sh.byv.task.TaskItem;
import sh.byv.task.TaskService;
import sh.byv.task.TaskType;
import sh.byv.zone.ZoneService;

import java.util.List;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class ZoneWorker implements JobWorker {

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

        final var task = new TaskItem();
        task.setType(TaskType.SIMULATION);
        task.setResultKey(new TaskItem.MapField("zone:%d:tick:%d:results".formatted(zoneId, tickId), "sim"));
        task.setSimulation(new TaskItem.Simulation(zoneId, tickId, "sim"));

        tasks.pushTask(task);

//        final var resultKey = CacheService.resultKey(entityId, tickId);
//
//        for (final var sim : activeSims) {
//            final var task = new CachedTask(entityId, tickId, sim.getId(), sim.getName(), resultKey);
//            cache.pushTask(task);
//        }
//
//        log.debug("Zone {} tick {}: pushed {} tasks", entityId, tickId, activeSims.size());
//
//        try {
//            Thread.sleep(100);
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//            return;
//        }
//
//        final var results = cache.readResults(entityId, tickId);
//        final var snapshot = new ZoneSnapshot(entityId, tickId, System.currentTimeMillis(), results);
//
//        cache.setZoneState(snapshot);
//        cache.publishZoneUpdate(snapshot);
//
//        log.debug("Zone {} tick {}: aggregated {} results", entityId, tickId, results.size());
    }

    List<SimEntity> fetchActiveSims(final Long zoneId) {
        final var zone = zones.getByIdRequired(zoneId);
        return sims.getActiveByZone(zone);
    }
}
