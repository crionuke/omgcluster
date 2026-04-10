package sh.byv.handler.event;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.conn.ConnZoneRelService;
import sh.byv.conn.ConnZoneRelStatus;
import sh.byv.event.EventEntity;
import sh.byv.event.EventHandler;
import sh.byv.event.EventService;
import sh.byv.event.EventType;

@Slf4j
@Transactional
@ApplicationScoped
@AllArgsConstructor
public class ConnZoneRelCreated implements EventHandler {

    final ConnZoneRelService rels;
    final EventService events;

    @Override
    public EventType getType() {
        return EventType.CONN_ZONE_REL_CREATED;
    }

    @Override
    public void execute(final EventEntity event) {
        final var rel = rels.getByIdRequired(event.getEntityId());
        if (rel.getStatus() == ConnZoneRelStatus.PENDING) {
            rel.setStatus(ConnZoneRelStatus.ACTIVE);
            events.create(EventType.CONN_ZONE_REL_ACTIVATED, rel.getId());
        }
    }
}
