package sh.byv.sim;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import sh.byv.event.EntityStatus;
import sh.byv.instance.InstanceEntity;

import java.time.OffsetDateTime;
import java.util.Optional;

@ApplicationScoped
public class SimInstanceRelRepository implements PanacheRepository<SimInstanceRelEntity> {

    SimInstanceRelEntity create(final SimEntity sim, final InstanceEntity instance) {
        final var rel = new SimInstanceRelEntity();
        rel.setSim(sim);
        rel.setInstance(instance);
        rel.setCreatedAt(OffsetDateTime.now());
        rel.setUpdatedAt(OffsetDateTime.now());
        rel.setStatus(EntityStatus.PENDING);
        persist(rel);
        return rel;
    }

    Optional<InstanceEntity> findLeastPopulatedInstance() {
        return getEntityManager()
                .createQuery("select s from InstanceEntity s left join SimInstanceRelEntity r on r.instance = s " +
                                "group by s order by count(r) asc",
                        InstanceEntity.class)
                .setMaxResults(1)
                .getResultList()
                .stream()
                .findFirst();
    }
}
