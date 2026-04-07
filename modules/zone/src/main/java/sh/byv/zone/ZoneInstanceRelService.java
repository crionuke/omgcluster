package sh.byv.zone;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import sh.byv.exception.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.event.EventService;
import sh.byv.event.EventType;
import sh.byv.instance.InstanceEntity;

@Slf4j
@Transactional
@ApplicationScoped
@AllArgsConstructor
public class ZoneInstanceRelService {

    final ZoneInstanceRelRepository repository;
    final EventService events;

    public ZoneInstanceRelEntity create(final ZoneEntity zone, final InstanceEntity instance) {
        final var rel = repository.create(zone, instance);
        events.create(EventType.ZONE_INSTANCE_REL_CREATED, rel.getId());
        log.info("Zone {} relation to instance {} created", zone.getId(), instance.getId());
        return rel;
    }

    public ZoneInstanceRelEntity getByIdRequired(final Long id) {
        return repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Rel not found: " + id));
    }

    public InstanceEntity getLeastPopulatedInstance() {
        return repository.findLeastPopulatedInstance()
                .orElseThrow(() -> new NotFoundException("No instances found"));
    }
}
