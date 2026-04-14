package sh.byv.lifecycle;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import sh.byv.event.EventService;
import sh.byv.event.EventType;
import sh.byv.node.NodeConfig;
import sh.byv.node.NodeService;
import sh.byv.job.JobService;
import sh.byv.job.JobType;
import sh.byv.mdc.WithMdcId;
import sh.byv.task.TaskService;

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
