package sh.byv.event.entity;

import io.quarkus.runtime.Startup;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Startup
@Transactional
@ApplicationScoped
public class EventService {

    final Map<EventType, EventHandler> handlers;
    final EventRepository repository;

    EventService(final EventRepository repository, final Instance<EventHandler> instances) {
        this.repository = repository;

        handlers = new ConcurrentHashMap<>();
        instances.stream().forEach(handler -> {
            final var type = handler.getType();
            handlers.put(type, handler);
        });

        log.info("Registered event handlers, {}", handlers.keySet());
    }

    public EventEntity create(final EventType type, final Long entityId) {
        return repository.create(type, entityId);
    }

    public List<EventEntity> getPendingEvents() {
        return repository.findByStatus(EventStatus.PENDING, 100);
    }

    public void handle(final EventEntity event) {
        final var handler = handlers.get(event.getType());
        final var eventId = event.getId();

        if (Objects.isNull(handler)) {
            log.error("No handler found for event {}, type={}", eventId, event.getType());
            repository.fail(event);
            return;
        }

        try {
            log.info("Handling {} event, entityId={}", event.getType(), event.getEntityId());
            handler.execute(event);
            repository.handle(event);
        } catch (Exception e) {
            log.error("Failed to handle event {}, type={}", eventId, event.getType(), e);
            repository.fail(event);
        }
    }
}
