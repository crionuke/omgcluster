package sh.byv.sim;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import sh.byv.event.EntityStatus;
import sh.byv.group.GroupEntity;

import java.time.OffsetDateTime;
import java.util.Optional;

@ApplicationScoped
public class SimGroupRelRepository implements PanacheRepository<SimGroupRelEntity> {

    SimGroupRelEntity create(final SimEntity sim, final GroupEntity group) {
        final var rel = new SimGroupRelEntity();
        rel.setSim(sim);
        rel.setGroup(group);
        rel.setCreatedAt(OffsetDateTime.now());
        rel.setUpdatedAt(OffsetDateTime.now());
        rel.setStatus(EntityStatus.PENDING);
        persist(rel);
        return rel;
    }

    Optional<GroupEntity> findLeastPopulatedGroup() {
        return getEntityManager()
                .createQuery("select g from GroupEntity g left join SimGroupRelEntity r on r.group = g " +
                                "group by g order by count(r) asc",
                        GroupEntity.class)
                .setMaxResults(1)
                .getResultList()
                .stream()
                .findFirst();
    }
}
