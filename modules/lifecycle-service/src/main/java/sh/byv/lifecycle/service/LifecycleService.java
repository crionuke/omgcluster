package sh.byv.lifecycle.service;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.server.entity.ServerService;
import sh.byv.job.service.JobService;
import sh.byv.mdc.id.WithMdcId;
import sh.byv.server.executor.TickService;
import sh.byv.task.executor.TaskExecutor;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class LifecycleService {

    final ServerService servers;
    final TaskExecutor tasks;
    final TickService ticks;
    final JobService jobs;

    @WithMdcId
    public void onStart(@Observes final StartupEvent event) {
        servers.start();
        jobs.start();
        ticks.start();
    }

    @WithMdcId
    public void onShutdown(@Observes final ShutdownEvent event) {
        ticks.shutdown();
        tasks.shutdown();
    }
}
