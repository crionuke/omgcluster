package sh.byv.worker;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.Instance;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@ApplicationScoped
public class WorkerService {

    final Map<String, WorkerRunner> workers;
    final ScheduledExecutorService executor;

    WorkerService(final Instance<WorkerRunner> workers) {
        this.workers = new ConcurrentHashMap<>();
        workers.stream().forEach(worker -> this.workers.put(worker.getName(), worker));

        this.executor = Executors.newScheduledThreadPool(this.workers.size());

        log.info("Registered worker runners, {}", this.workers.keySet());
    }

    void onStart(@Observes final StartupEvent event) {
        workers.values().forEach(worker -> {
            final var interval = worker.getInterval().toSeconds();
            executor.scheduleAtFixedRate(worker, interval, interval, TimeUnit.SECONDS);
            log.info("Started {} with interval {}s", worker.getName(), interval);
        });
    }

    void onStop(@Observes final ShutdownEvent event) {
        executor.shutdown();
        log.info("Stopped");
    }
}