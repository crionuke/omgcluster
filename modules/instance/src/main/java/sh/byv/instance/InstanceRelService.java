package sh.byv.instance;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.event.EventService;
import sh.byv.event.EventType;
import sh.byv.exception.NotFoundException;

@Slf4j
@Transactional
@ApplicationScoped
@AllArgsConstructor
public class InstanceRelService {

    final InstanceRelRepository repository;
    final EventService events;

    public InstanceRelEntity create(final InstanceRelType type, final Long entityId, final InstanceEntity instance) {
        final var rel = repository.create(instance, type, entityId);
        events.create(EventType.INSTANCE_REL_CREATED, rel.getId());
        log.info("{} {} relation to instance {} created", type, entityId, instance.getId());
        return rel;
    }

    public InstanceRelEntity getByIdRequired(final Long id) {
        return repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Rel not found: " + id));
    }

    public InstanceRelEntity getByZoneRequired(final Long zoneId) {
        return repository.findByTypeAndEntity(InstanceRelType.ZONE, zoneId)
                .orElseThrow(() -> new NotFoundException("Rel not found: ZONE:" + zoneId));
    }

    public InstanceEntity getLeastPopulatedInstance() {
        return repository.findLeastPopulatedInstance()
                .orElseThrow(() -> new NotFoundException("No instances found"));
    }

    public void activate(final InstanceRelEntity rel) {
        rel.setStatus(InstanceRelStatus.ACTIVE);
        events.create(EventType.INSTANCE_REL_ACTIVATED, rel.getId());
    }
}
