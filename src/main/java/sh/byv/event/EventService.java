package sh.byv.event;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@ApplicationScoped
public class EventService {

    final Map<EventType, EventHandler> handlers;
    final EventRepository repository;

    EventService(final EventRepository repository, final Instance<EventHandler> handlers) {
        this.repository = repository;

        this.handlers = new ConcurrentHashMap<>();
        handlers.stream().forEach(handler -> {
            final var type = handler.getType();
            this.handlers.put(type, handler);
        });

        log.info("Registered event handlers, {}", this.handlers.keySet());
    }

    @Transactional
    public EventEntity create(final EventType type, final Long entityId) {
        return repository.create(type, entityId);
    }

    @Transactional
    public List<EventEntity> fetchPending() {
        return repository.fetchByStatus(EventStatus.PENDING, 100);
    }

    @Transactional
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
