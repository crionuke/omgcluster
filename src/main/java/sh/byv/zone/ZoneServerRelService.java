package sh.byv.zone;

import jakarta.enterprise.context.ApplicationScoped;
import sh.byv.exception.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.event.EventService;
import sh.byv.event.EventType;
import sh.byv.server.ServerEntity;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class ZoneServerRelService {

    final ZoneServerRelRepository repository;
    final EventService events;

    public ZoneServerRelEntity create(final ZoneEntity zone, final ServerEntity server) {
        final var rel = repository.create(zone, server);
        events.create(EventType.ZONE_SERVER_REL_CREATED, rel.getId());
        log.info("Zone {} relation to server {} created", zone.getId(), server.getId());
        return rel;
    }

    public ZoneServerRelEntity getByIdRequired(final Long id) {
        return repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Rel not found: " + id));
    }

    public ServerEntity getLeastPopulatedServer() {
        return repository.findLeastPopulatedServer()
                .orElseThrow(() -> new NotFoundException("No servers found"));
    }
}
