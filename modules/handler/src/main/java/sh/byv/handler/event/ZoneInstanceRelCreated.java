package sh.byv.handler.event;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.event.EventEntity;
import sh.byv.event.EventHandler;
import sh.byv.event.EventService;
import sh.byv.event.EventType;
import sh.byv.state.StateService;
import sh.byv.zone.ZoneInstanceRelService;
import sh.byv.zone.ZoneInstanceRelStatus;

@Slf4j
@Transactional
@ApplicationScoped
@AllArgsConstructor
public class ZoneInstanceRelCreated implements EventHandler {

    final ZoneInstanceRelService rels;
    final EventService events;
    final StateService state;

    @Override
    public EventType getType() {
        return EventType.ZONE_INSTANCE_REL_CREATED;
    }

    @Override
    public void execute(final EventEntity event) {
        final var rel = rels.getByIdRequired(event.getEntityId());
        if (rel.getStatus() == ZoneInstanceRelStatus.PENDING) {
            rel.setStatus(ZoneInstanceRelStatus.ACTIVE);
            events.create(EventType.ZONE_INSTANCE_REL_ACTIVATED, rel.getId());

            state.addZone(rel.getInstance(), rel.getZone());
        }
    }
}
