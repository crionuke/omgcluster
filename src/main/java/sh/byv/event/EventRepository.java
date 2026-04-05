package sh.byv.event;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.OffsetDateTime;
import java.util.List;

@ApplicationScoped
public class EventRepository implements PanacheRepository<EventEntity> {

    public EventEntity create(final EventType type,
                              final Long entityId) {
        final var event = new EventEntity();
        event.setCreatedAt(OffsetDateTime.now());
        event.setUpdatedAt(OffsetDateTime.now());
        event.setType(type);
        event.setEntityId(entityId);
        event.setStatus(EventStatus.PENDING);
        persist(event);
        return event;
    }

    public EventEntity process(final EventEntity event) {
        event.setUpdatedAt(OffsetDateTime.now());
        event.setStatus(EventStatus.PROCESSED);
        return event;
    }

    public List<EventEntity> fetchByStatus(final EventStatus status, final int limit) {
        return find("status = ?1 order by createdAt asc", status)
                .page(0, limit)
                .list();
    }

    public EventEntity fail(final EventEntity event) {
        event.setUpdatedAt(OffsetDateTime.now());
        event.setStatus(EventStatus.FAILED);
        return event;
    }
}
