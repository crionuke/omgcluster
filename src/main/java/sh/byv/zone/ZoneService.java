package sh.byv.zone;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.event.EventService;
import sh.byv.event.EventType;
import sh.byv.layer.LayerEntity;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class ZoneService {

    final ZoneRepository zoneRepository;
    final EventService eventService;

    @Transactional
    public ZoneEntity create(final LayerEntity layer,
                             final int x1,
                             final int y1,
                             final int x2,
                             final int y2) {
        final var zone = zoneRepository.create(layer, x1, y1, x2, y2);
        eventService.create(EventType.ZONE_CREATED, zone.getId());
        log.info("Created zone [{},{} -> {},{}] in layer {}", x1, y1, x2, y2, layer.getName());
        return zone;
    }
}
