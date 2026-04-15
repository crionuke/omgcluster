package sh.byv.signal.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.pubsub.PubSubCommands;
import io.quarkus.runtime.Startup;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import lombok.extern.slf4j.Slf4j;

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

    SignalService(final RedisDataSource redis, final ObjectMapper objectMapper,
                  final Instance<SignalHandler> instances) {
        this.signals = redis.pubsub(String.class);
        this.mapper = objectMapper;

        handlers = new ConcurrentHashMap<>();
        instances.stream().forEach(handler -> handlers.put(handler.getType(), handler));
        log.info("Registered signal handlers: {}", handlers.keySet());

        signals.subscribe(CHANNEL, this::onSignal);
        log.info("Subscribed to signal channel: {}", CHANNEL);
    }

    public void tick(final long zoneId, final long tickId) {
        publish(new SignalBody(SignalType.TICK, new SignalBody.TickBody(zoneId, tickId)));
    }

    void publish(final SignalBody signal) {
        try {
            final var json = mapper.writeValueAsString(signal);
            signals.publish(CHANNEL, json);
        } catch (Exception e) {
            log.error("Failed to publish signal: {}", signal, e);
        }
    }

    void onSignal(final String json) {
        try {
            final var signal = mapper.readValue(json, SignalBody.class);
            final var handler = handlers.get(signal.getType());
            if (handler != null) {
                handler.execute(signal);
            } else {
                log.warn("No handler for signal type: {}", signal.getType());
            }
        } catch (Exception e) {
            log.error("Failed to handle signal: {}", json, e);
        }
    }
}