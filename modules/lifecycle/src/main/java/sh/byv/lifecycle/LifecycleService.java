package sh.byv.lifecycle;

import io.quarkus.runtime.StartupEvent;
import io.quarkus.scheduler.Scheduled;
import io.quarkus.scheduler.Scheduler;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import sh.byv.command.CommandProcessor;
import sh.byv.event.EventService;
import sh.byv.event.EventType;
import sh.byv.instance.InstanceConfig;
import sh.byv.instance.InstanceService;
import sh.byv.job.JobService;
import sh.byv.job.JobType;
import sh.byv.mdc.WithMdcId;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class LifecycleService {

    final CommandProcessor processor;
    final InstanceService instances;
    final InstanceConfig config;
    final Scheduler scheduler;
    final EventService events;
    final JobService jobs;

    @WithMdcId
    public void onStart(@Observes final StartupEvent event) throws SchedulerException {
        final var name = config.name();
        log.info("Create instance {}", name);
        final var instance = instances.getOrCreate(name);

        final var trigger = scheduler.newJob("commands")
                .setConcurrentExecution(Scheduled.ConcurrentExecution.SKIP)
                .setInterval("1s")
                .setExecuteWith(Scheduled.SIMPLE)
                .setTask(_ -> processor.process())
                .schedule();

        log.info("Command processor scheduled {}", trigger);

        jobs.iterate(JobType.EVENT);

        events.create(EventType.INSTANCE_STARTED, instance.getId());
    }
}
