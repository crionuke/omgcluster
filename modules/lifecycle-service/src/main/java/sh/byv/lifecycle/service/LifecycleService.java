package sh.byv.lifecycle.service;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import sh.byv.event.entity.EventService;
import sh.byv.event.entity.EventType;
import sh.byv.node.entity.NodeConfig;
import sh.byv.node.entity.NodeService;
import sh.byv.job.service.JobService;
import sh.byv.mdc.id.WithMdcId;
import sh.byv.task.service.TaskService;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class LifecycleService {

    final NodeService nodes;
    final NodeConfig config;
    final EventService events;
    final TaskService tasks;
    final JobService jobs;

    @WithMdcId
    public void onStart(@Observes final StartupEvent event) throws SchedulerException {
        final var name = config.name();
        log.info("Create node {}", name);
        final var node = nodes.getOrCreate(name);

        jobs.start();
        tasks.start();

        events.create(EventType.NODE_STARTED, node.getId());
    }

    @WithMdcId
    public void onStart(@Observes final ShutdownEvent event) throws SchedulerException {
        tasks.shutdown();
    }
}
