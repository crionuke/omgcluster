package sh.byv.node.entity;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.OffsetDateTime;
import java.util.Optional;

@ApplicationScoped
public class NodeRepository implements PanacheRepository<NodeEntity> {

    NodeEntity create(final String name) {
        final var node = new NodeEntity();
        node.setCreatedAt(OffsetDateTime.now());
        node.setUpdatedAt(OffsetDateTime.now());
        node.setName(name);
        node.setStatus(NodeStatus.PENDING);
        persist(node);
        return node;
    }

    Optional<NodeEntity> findByName(final String name) {
        return find("name", name).firstResultOptional();
    }
}
