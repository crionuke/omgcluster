package sh.byv.node.entity;

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
public class NodeRelService {

    final NodeRelRepository repository;
    final EventService events;

    public NodeRelEntity create(final NodeRelType type, final Long entityId, final NodeEntity node) {
        final var rel = repository.create(node, type, entityId);
        events.create(EventType.NODE_REL_CREATED, rel.getId());
        log.info("{} {} relation to node {} created", type, entityId, node.getId());
        return rel;
    }

    public NodeRelEntity getByIdRequired(final Long id) {
        return repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Rel not found: " + id));
    }

    public NodeRelEntity getByZoneRequired(final Long zoneId) {
        return repository.findByTypeAndEntity(NodeRelType.ZONE, zoneId)
                .orElseThrow(() -> new NotFoundException("Rel not found: ZONE:" + zoneId));
    }

    public NodeEntity getLeastPopulatedNode() {
        return repository.findLeastPopulatedNode()
                .orElseThrow(() -> new NotFoundException("No nodes found"));
    }

    public void activate(final NodeRelEntity rel) {
        rel.setStatus(NodeRelStatus.ACTIVE);
        events.create(EventType.NODE_REL_ACTIVATED, rel.getId());
    }
}
