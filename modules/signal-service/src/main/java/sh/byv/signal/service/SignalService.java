package sh.byv.signal.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.pubsub.PubSubCommands;
import io.quarkus.runtime.Startup;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import lombok.extern.slf4j.Slf4j;
import sh.byv.task.executor.TaskExecutor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Startup
@ApplicationScoped
public class SignalService {

    static final String CHANNEL = "omgs:signals";

    final Map<SignalType, SignalHandler> handlers;
    final PubSubCommands<String> signals;
    final ObjectMapper mapper;
    final TaskExecutor tasks;

    SignalService(final TaskExecutor tasks,
                  final RedisDataSource redis,
                  final ObjectMapper mapper,
                  final Instance<SignalHandler> instances) {
        this.signals = redis.pubsub(String.class);
        this.mapper = mapper;
        this.tasks = tasks;

        handlers = new ConcurrentHashMap<>();
        instances.stream().forEach(handler -> handlers.put(handler.getType(), handler));
        log.info("Registered signal handlers: {}", handlers.keySet());

        signals.subscribe(CHANNEL, this::onSignal);
        log.info("Subscribed to signal channel: {}", CHANNEL);
    }

    public void publish(final SignalEnvelope envelope) {
        if (envelope.getSignals() == null || envelope.getSignals().isEmpty()) {
            return;
        }

        try {
            final var json = mapper.writeValueAsString(envelope);
            signals.publish(CHANNEL, json);
        } catch (Exception e) {
            log.error("Failed to publish signal envelope: {}", e.getMessage(), e);
        }
    }

    void onSignal(final String json) {
        final SignalEnvelope envelope;
        try {
            envelope = mapper.readValue(json, SignalEnvelope.class);
        } catch (Exception e) {
            log.error("Failed to parse signal envelope {}: {}", json, e.getMessage(), e);
            return;
        }

        if (envelope.getSignals() == null) {
            return;
        }

        envelope.getSignals().forEach(this::dispatch);
    }

    void dispatch(final SignalBody signal) {
        try {
            final var handler = handlers.get(signal.getType());
            if (handler != null) {
                tasks.execute(() -> handler.execute(signal));
            } else {
                log.warn("No handler for signal type: {}", signal.getType());
            }
        } catch (Exception e) {
            log.error("Failed to handle signal {}: {}", signal, e.getMessage(), e);
        }
    }
}