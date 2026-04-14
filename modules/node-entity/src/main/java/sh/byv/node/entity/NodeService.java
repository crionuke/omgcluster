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
public class NodeService {

    final NodeRepository repository;
    final NodeConfig config;
    final EventService events;

    public NodeEntity create(final String name) {
        final var node = repository.create(name);
        events.create(EventType.NODE_CREATED, node.getId());
        log.info("Node {} created with id {}", name, node.getId());
        return node;
    }

    public NodeEntity getByIdRequired(final Long id) {
        return repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Node not found: " + id));
    }

    public NodeEntity getByNameRequired(final String name) {
        return repository.findByName(name)
                .orElseThrow(() -> new NotFoundException("Node not found: " + name));
    }

    public NodeEntity getThisNode() {
        return getByNameRequired(config.name());
    }

    public void activate(final NodeEntity node) {
        node.setStatus(NodeStatus.ACTIVE);
        events.create(EventType.NODE_ACTIVATED, node.getId());
    }

    public NodeEntity getOrCreate(final String name) {
        final var existing = repository.findByName(name);
        if (existing.isPresent()) {
            log.info("Node {} already exists with id {}", name, existing.get().getId());
            return existing.get();
        }

        return create(name);
    }
}
