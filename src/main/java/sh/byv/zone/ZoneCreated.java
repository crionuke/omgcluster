package sh.byv.zone;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.event.EntityStatus;
import sh.byv.event.EventEntity;
import sh.byv.event.EventHandler;
import sh.byv.event.EventType;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class ZoneCreated implements EventHandler {

    final ZoneCreated thisHandler;
    final ZoneService zoneService;

    @Override
    public EventType getType() {
        return EventType.ZONE_CREATED;
    }

    @Override
    public void execute(final EventEntity event) {
        thisHandler.handle(event.getEntityId());
    }

    @Transactional
    public void handle(final Long zoneId) {
        final var zone = zoneService.getByIdRequired(zoneId);
        if (zone.getStatus() == EntityStatus.PENDING) {
            zone.setStatus(EntityStatus.CREATED);
        }
    }
}