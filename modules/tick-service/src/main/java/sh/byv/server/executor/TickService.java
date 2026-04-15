package sh.byv.server.executor;

import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.value.ValueCommands;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import sh.byv.signal.service.SignalService;
import sh.byv.state.entity.StateBody;
import sh.byv.state.entity.StateCache;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@ApplicationScoped
public class TickService {

    final ValueCommands<String, Long> longCommands;
    final ScheduledExecutorService scheduler;
    final SignalService signals;
    final StateCache cache;

    public TickService(final StateCache cache, final SignalService signals, final RedisDataSource redis) {
        this.cache = cache;
        this.signals = signals;
        this.longCommands = redis.value(Long.class);

        scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    public void start() {
        scheduler.scheduleAtFixedRate(() -> cache.getThisServerState()
                .ifPresent(this::tick), 0, 1000, TimeUnit.MILLISECONDS);
    }

    public void shutdown() {
        scheduler.shutdown();
    }

    public long getZoneTick(final long zoneId) {
        return longCommands.get("omgc:zone:%d".formatted(zoneId));
    }

    void tick(final StateBody state) {
        state.getZones().forEach((zoneId, _) -> {
            final var tickId = longCommands.incr("omgc:zone:%d".formatted(zoneId));
            signals.tick(zoneId, tickId);
        });
    }
}
