package sh.byv.event.handler;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.conn.entity.ConnZoneRelService;
import sh.byv.conn.entity.ConnZoneRelStatus;
import sh.byv.event.entity.EventEntity;
import sh.byv.event.entity.EventHandler;
import sh.byv.event.entity.EventType;

@Slf4j
@Transactional
@ApplicationScoped
@AllArgsConstructor
public class ConnZoneRelCreated implements EventHandler {

    final ConnZoneRelService rels;

    @Override
    public EventType getType() {
        return EventType.CONN_ZONE_REL_CREATED;
    }

    @Override
    public void execute(final EventEntity event) {
        final var rel = rels.getByIdRequired(event.getEntityId());
        if (rel.getStatus() == ConnZoneRelStatus.PENDING) {
            rels.activate(rel);
        }
    }
}
