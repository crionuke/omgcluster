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
@Transactional
@ApplicationScoped
@AllArgsConstructor
public class ZoneCreated implements EventHandler {

    final ZoneInstanceRelService rels;
    final ZoneService zones;

    @Override
    public EventType getType() {
        return EventType.ZONE_CREATED;
    }

    @Override
    public void execute(final EventEntity event) {
        final var zone = zones.getByIdRequired(event.getEntityId());
        if (zone.getStatus() == EntityStatus.PENDING) {
            zone.setStatus(EntityStatus.CREATED);

            final var instance = rels.getLeastPopulatedInstance();
            rels.create(zone, instance);
        }
    }
}
