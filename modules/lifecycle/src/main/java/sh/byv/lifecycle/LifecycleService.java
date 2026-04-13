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
import sh.byv.instance.InstanceConfig;
import sh.byv.instance.InstanceService;
import sh.byv.job.JobService;
import sh.byv.job.JobType;
import sh.byv.mdc.WithMdcId;
import sh.byv.task.TaskService;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class LifecycleService {

    final InstanceService instances;
    final InstanceConfig config;
    final EventService events;
    final TaskService tasks;
    final JobService jobs;

    @WithMdcId
    public void onStart(@Observes final StartupEvent event) throws SchedulerException {
        final var name = config.name();
        log.info("Create instance {}", name);
        final var instance = instances.getOrCreate(name);

        jobs.schedule(JobType.EVENT);
        tasks.start();

        events.create(EventType.INSTANCE_STARTED, instance.getId());
    }

    @WithMdcId
    public void onStart(@Observes final ShutdownEvent event) throws SchedulerException {
        tasks.shutdown();
    }
}
