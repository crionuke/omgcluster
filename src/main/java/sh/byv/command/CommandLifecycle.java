package sh.byv.command;

import io.quarkus.runtime.StartupEvent;
import io.quarkus.scheduler.Scheduled;
import io.quarkus.scheduler.Scheduler;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.mdc.WithMdcId;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class CommandLifecycle {

    final CommandWorker worker;
    final Scheduler scheduler;

    @WithMdcId
    public void onStart(@Observes @Priority(2) final StartupEvent event) {
        final var trigger = scheduler.newJob("command-worker")
                .setConcurrentExecution(Scheduled.ConcurrentExecution.SKIP)
                .setInterval("1s")
                .setExecuteWith(Scheduled.SIMPLE)
                .setTask(scheduledExecution -> worker.execute());

        log.info("Command worker scheduled {}", trigger);
    }
}
