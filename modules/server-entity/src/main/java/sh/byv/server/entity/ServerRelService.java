package sh.byv.server.entity;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.event.entity.EventService;
import sh.byv.event.entity.EventType;
import sh.byv.exception.clazz.NotFoundException;

@Slf4j
@Transactional
@ApplicationScoped
@AllArgsConstructor
public class ServerRelService {

    final ServerRelRepository repository;
    final EventService events;

    public ServerRelEntity create(final ServerRelType type, final Long entityId, final ServerEntity server) {
        final var rel = repository.create(server, type, entityId);
        events.create(EventType.SERVER_REL_CREATED, rel.getId());
        log.info("{} {} relation to server {} created", type, entityId, server.getId());
        return rel;
    }

    public ServerRelEntity getByIdRequired(final Long id) {
        return repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Rel not found: " + id));
    }

    public ServerRelEntity getByZoneRequired(final Long zoneId) {
        return repository.findByTypeAndEntity(ServerRelType.ZONE, zoneId)
                .orElseThrow(() -> new NotFoundException("Rel not found: ZONE:" + zoneId));
    }

    public ServerEntity getLeastPopulatedServer() {
        return repository.findLeastPopulatedServer()
                .orElseThrow(() -> new NotFoundException("No servers found"));
    }

    public void activate(final ServerRelEntity rel) {
        rel.setStatus(ServerRelStatus.ACTIVE);
        events.create(EventType.SERVER_REL_ACTIVATED, rel.getId());
    }
}
