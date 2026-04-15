package sh.byv.server.executor;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import sh.byv.state.entity.StateCache;
import sh.byv.state.entity.StateBody;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@ApplicationScoped
public class TickService {

    final ScheduledExecutorService scheduler;
    final StateCache cache;

    public TickService(final StateCache cache) {
        this.cache = cache;
        scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    public void start() {
        scheduler.scheduleAtFixedRate(() -> cache.getThisServerState().ifPresent(this::tick), 0, 1000,
                TimeUnit.MILLISECONDS);
    }

    public void shutdown() {
        scheduler.shutdown();
    }

    void tick(final StateBody state) {
        log.info("Ticking state: {}", state);
    }
}
