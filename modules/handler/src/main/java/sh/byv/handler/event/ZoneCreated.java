package sh.byv.handler.event;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.event.EntityStatus;
import sh.byv.event.EventEntity;
import sh.byv.event.EventHandler;
import sh.byv.event.EventType;
import sh.byv.zone.ZoneInstanceRelService;
import sh.byv.zone.ZoneService;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class ZoneCreated implements EventHandler {

    final ZoneInstanceRelService rels;
    final ZoneCreated proxy;
    final ZoneService zones;

    @Override
    public EventType getType() {
        return EventType.ZONE_CREATED;
    }

    @Override
    public void execute(final EventEntity event) {
        proxy.handle(event.getEntityId());
    }

    @Transactional
    public void handle(final Long zoneId) {
        final var zone = zones.getByIdRequired(zoneId);
        if (zone.getStatus() == EntityStatus.PENDING) {
            final var instance = rels.getLeastPopulatedInstance();
            rels.create(zone, instance);
            zone.setStatus(EntityStatus.CREATED);
        }
    }
}
