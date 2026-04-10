package sh.byv.instance;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.OffsetDateTime;
import java.util.Optional;

@ApplicationScoped
public class InstanceRelRepository implements PanacheRepository<InstanceRelEntity> {

    InstanceRelEntity create(final InstanceEntity instance,
                             final InstanceRelType type,
                             final Long entityId) {
        final var rel = new InstanceRelEntity();
        rel.setInstance(instance);
        rel.setCreatedAt(OffsetDateTime.now());
        rel.setUpdatedAt(OffsetDateTime.now());
        rel.setType(type);
        rel.setEntityId(entityId);
        rel.setStatus(InstanceRelStatus.PENDING);
        persist(rel);
        return rel;
    }

    Optional<InstanceRelEntity> findByTypeAndEntity(final InstanceRelType type, final Long entityId) {
        return find("type = ?1 and entityId = ?2", type, entityId).firstResultOptional();
    }

    Optional<InstanceEntity> findLeastPopulatedInstance() {
        return getEntityManager()
                .createQuery("select s from InstanceEntity s left join InstanceRelEntity r on r.instance = s " +
                                "group by s order by count(r) asc",
                        InstanceEntity.class)
                .setMaxResults(1)
                .getResultList()
                .stream()
                .findFirst();
    }
}
