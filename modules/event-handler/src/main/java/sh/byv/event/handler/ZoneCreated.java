package sh.byv.event.handler;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.event.entity.EventEntity;
import sh.byv.event.entity.EventHandler;
import sh.byv.event.entity.EventType;
import sh.byv.server.entity.ServerRelService;
import sh.byv.server.entity.ServerRelType;
import sh.byv.zone.entity.ZoneService;
import sh.byv.zone.entity.ZoneStatus;

@Slf4j
@Transactional
@ApplicationScoped
@AllArgsConstructor
public class ZoneCreated implements EventHandler {

    final ServerRelService rels;
    final ZoneService zones;

    @Override
    public EventType getType() {
        return EventType.ZONE_CREATED;
    }

    @Override
    public void execute(final EventEntity event) {
        final var zone = zones.getByIdRequired(event.getEntityId());
        if (zone.getStatus() == ZoneStatus.PENDING) {
            final var server = rels.getLeastPopulatedServer();
            rels.create(ServerRelType.ZONE, zone.getId(), server);
            zones.activate(zone);
        }
    }
}
