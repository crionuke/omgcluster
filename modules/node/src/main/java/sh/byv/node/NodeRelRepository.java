package sh.byv.node;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.OffsetDateTime;
import java.util.Optional;

@ApplicationScoped
public class NodeRelRepository implements PanacheRepository<NodeRelEntity> {

    NodeRelEntity create(final NodeEntity node,
                         final NodeRelType type,
                         final Long entityId) {
        final var rel = new NodeRelEntity();
        rel.setNode(node);
        rel.setCreatedAt(OffsetDateTime.now());
        rel.setUpdatedAt(OffsetDateTime.now());
        rel.setType(type);
        rel.setEntityId(entityId);
        rel.setStatus(NodeRelStatus.PENDING);
        persist(rel);
        return rel;
    }

    Optional<NodeRelEntity> findByTypeAndEntity(final NodeRelType type, final Long entityId) {
        return find("type = ?1 and entityId = ?2", type, entityId).firstResultOptional();
    }

    Optional<NodeEntity> findLeastPopulatedNode() {
        return getEntityManager()
                .createQuery("select s from NodeEntity s left join NodeRelEntity r on r.node = s " +
                                "group by s order by count(r) asc",
                        NodeEntity.class)
                .setMaxResults(1)
                .getResultList()
                .stream()
                .findFirst();
    }
}
