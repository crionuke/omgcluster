package sh.byv.zone;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import sh.byv.instance.InstanceEntity;

import java.time.OffsetDateTime;
import java.util.Optional;

@ApplicationScoped
public class ZoneInstanceRelRepository implements PanacheRepository<ZoneInstanceRelEntity> {

    ZoneInstanceRelEntity create(final ZoneEntity zone, final InstanceEntity instance) {
        final var rel = new ZoneInstanceRelEntity();
        rel.setZone(zone);
        rel.setInstance(instance);
        rel.setCreatedAt(OffsetDateTime.now());
        rel.setUpdatedAt(OffsetDateTime.now());
        rel.setStatus(ZoneInstanceRelStatus.PENDING);
        persist(rel);
        return rel;
    }

    Optional<InstanceEntity> findLeastPopulatedInstance() {
        return getEntityManager()
                .createQuery("select s from InstanceEntity s left join ZoneInstanceRelEntity r on r.instance = s " +
                                "group by s order by count(r) asc",
                        InstanceEntity.class)
                .setMaxResults(1)
                .getResultList()
                .stream()
                .findFirst();
    }
}
