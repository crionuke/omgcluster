package sh.byv.worker;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@ApplicationScoped
public class WorkerService {

    final ScheduledExecutorService scheduler;

    public WorkerService() {
        this.scheduler = Executors.newScheduledThreadPool(4);
    }

    public void scheduleNonOverlapping(final Runnable worker,
                                       final long initialDelay,
                                       final long period,
                                       final TimeUnit unit) {
        final var running = new AtomicBoolean(false);
        final var future = scheduler.scheduleAtFixedRate(() -> {
            if (!running.compareAndSet(false, true)) {
                return;
            }

            try {
                worker.run();
            } finally {
                running.set(false);
            }
        }, initialDelay, period, unit);
    }
}