package sh.byv.zone.executor;

import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import sh.byv.state.entity.StateBody;
import sh.byv.state.entity.StateService;

@ApplicationScoped
@AllArgsConstructor
public class ExecutorJob {

    final ZoneExecutor zoneExecutors;
    final StateService states;

    @Scheduled(every = "1s", concurrentExecution = Scheduled.ConcurrentExecution.SKIP, executeWith = Scheduled.SIMPLE)
    public void schedule() {
        final StateBody state = states.getThisState();
        zoneExecutors.schedule(state);
    }
}
