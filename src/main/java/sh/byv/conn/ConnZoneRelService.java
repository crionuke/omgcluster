package sh.byv.conn;

import jakarta.enterprise.context.ApplicationScoped;
import sh.byv.exception.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.event.EventService;
import sh.byv.event.EventType;
import sh.byv.zone.ZoneEntity;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class ConnZoneRelService {

    final ConnZoneRelRepository repository;
    final EventService events;

    public ConnZoneRelEntity create(final ConnEntity conn, final ZoneEntity zone) {
        final var rel = repository.create(conn, zone);
        events.create(EventType.CONN_ZONE_REL_CREATED, rel.getId());
        log.info("Conn {} relation to zone {} created", conn.getId(), zone.getId());
        return rel;
    }

    public ConnZoneRelEntity getByIdRequired(final Long id) {
        return repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Rel not found: " + id));
    }
}
