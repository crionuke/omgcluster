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
import sh.byv.server.entity.ServerConfig;
import sh.byv.server.entity.ServerService;
import sh.byv.job.service.JobService;
import sh.byv.mdc.id.WithMdcId;
import sh.byv.task.service.TaskService;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class LifecycleService {

    final ServerService servers;
    final ServerConfig config;
    final EventService events;
    final TaskService tasks;
    final JobService jobs;

    @WithMdcId
    public void onStart(@Observes final StartupEvent event) throws SchedulerException {
        final var name = config.name();
        log.info("Create server {}", name);
        final var server = servers.getOrCreate(name);

        jobs.start();
        tasks.start();

        events.create(EventType.SERVER_STARTED, server.getId());
    }

    @WithMdcId
    public void onStart(@Observes final ShutdownEvent event) throws SchedulerException {
        tasks.shutdown();
    }
}
