package sh.byv.event;

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

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void process(final Long id) {
        final var event = repository.findById(id);
        final var handler = handlers.get(event.getType());

        if (Objects.isNull(handler)) {
            log.error("No handler found for event {}, type={}", id, event.getType());
            repository.fail(event);
            return;
        }

        try {
            log.info("Processing {} event, entityId={}", event.getType(), event.getEntityId());
            handler.execute(event);
            repository.process(event);
        } catch (Exception e) {
            log.error("Failed to process event {}, type={}", id, event.getType(), e);
            repository.fail(event);
        }
    }
}
