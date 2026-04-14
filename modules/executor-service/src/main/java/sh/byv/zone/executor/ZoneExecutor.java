package sh.byv.zone.executor;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import sh.byv.state.entity.StateBody;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j
@ApplicationScoped
public class ZoneExecutor {

    private final Map<Long, Future<?>> futures;
    private final ExecutorService executor;

    public ZoneExecutor() {
        this.executor = Executors.newCachedThreadPool();
        this.futures = new HashMap<>();
    }

    public synchronized void schedule(final StateBody state) {
        futures.keySet().stream()
                .filter(zoneId -> !state.hasZone(zoneId))
                .toList()
                .forEach(zoneId -> {
                    final var future = futures.remove(zoneId);
                    if (future != null) {
                        log.info("Cancel zone executor, zoneId={}", zoneId);
                        future.cancel(true);
                    }
                });

        state.getZones().keySet().stream()
                .filter(zoneId -> !futures.containsKey(zoneId))
                .forEach(zoneId -> {
                    log.info("Submit zone executor, zoneId={}", zoneId);
                    final var future = executor.submit(() -> runZone(zoneId));
                    futures.put(zoneId, future);
                });
    }

    public synchronized void shutdown() {
        futures.values().forEach(future -> future.cancel(true));
        futures.clear();
        executor.shutdownNow();
    }

    void runZone(final Long zoneId) {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Thread.sleep(100);
            }
        } catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
            log.info("Zone executor interrupted, zoneId={}", zoneId);
        } catch (final Exception e) {
            log.error("Zone executor failed, zoneId={}", zoneId, e);
        }
    }
}