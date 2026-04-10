package sh.byv.handler.event;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.event.EventEntity;
import sh.byv.event.EventHandler;
import sh.byv.event.EventService;
import sh.byv.event.EventType;
import sh.byv.instance.InstanceRelService;
import sh.byv.instance.InstanceRelType;
import sh.byv.zone.ZoneEntity;
import sh.byv.zone.ZoneService;
import sh.byv.zone.ZoneStatus;

@Slf4j
@Transactional
@ApplicationScoped
@AllArgsConstructor
public class ZoneCreated implements EventHandler {

    final InstanceRelService rels;
    final EventService events;
    final ZoneService zones;

    @Override
    public EventType getType() {
        return EventType.ZONE_CREATED;
    }

    @Override
    public void execute(final EventEntity event) {
        final var zone = zones.getByIdRequired(event.getEntityId());
        if (zone.getStatus() == ZoneStatus.PENDING) {
            final var instance = rels.getLeastPopulatedInstance();
            rels.create(InstanceRelType.ZONE, zone.getId(), instance);

            zones.activate(zone);
        }
    }
}
