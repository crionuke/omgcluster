package sh.byv.zone;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.event.EventService;
import sh.byv.event.EventType;
import sh.byv.exception.NotFoundException;
import sh.byv.layer.LayerEntity;

@Slf4j
@Transactional
@ApplicationScoped
@AllArgsConstructor
public class ZoneService {

    final ZoneRepository repository;
    final EventService events;

    public ZoneEntity createRoot(final LayerEntity layer,
                                 final int x1,
                                 final int y1,
                                 final int x2,
                                 final int y2) {
        final var zone = create(layer, null, x1, y1, x2, y2);
        log.info("Creating root zone [{},{} -> {},{}] in layer {} in world {}", x1, y1, x2, y2,
                layer.getName(), layer.getWorld().getName());
        return zone;
    }

    public ZoneEntity createChild(final ZoneEntity parent,
                                  final int x1,
                                  final int y1,
                                  final int x2,
                                  final int y2) {
        final var layer = parent.getLayer();
        final var zone = create(layer, parent, x1, y1, x2, y2);
        log.info("Creating child zone [{},{} -> {},{}] under parent zone {} in layer {} in world {}", x1, y1, x2, y2,
                parent.getId(), layer.getName(), layer.getWorld().getName());
        return zone;
    }

    public ZoneEntity getByIdRequired(final Long id) {
        return repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Zone not found: " + id));
    }

    public void activate(final ZoneEntity zone) {
        zone.setStatus(ZoneStatus.ACTIVE);
        events.create(EventType.ZONE_ACTIVATED, zone.getId());
    }

    ZoneEntity create(final LayerEntity layer,
                      final ZoneEntity parent,
                      final int x1,
                      final int y1,
                      final int x2,
                      final int y2) {
        final var zone = repository.create(layer, parent, x1, y1, x2, y2);
        events.create(EventType.ZONE_CREATED, zone.getId());
        return zone;
    }
}
