package sh.byv.zone;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import sh.byv.event.EntityStatus;
import sh.byv.server.ServerEntity;

import java.time.OffsetDateTime;
import java.util.Optional;

@ApplicationScoped
public class ZoneServerRelRepository implements PanacheRepository<ZoneServerRelEntity> {

    ZoneServerRelEntity create(final ZoneEntity zone, final ServerEntity server) {
        final var rel = new ZoneServerRelEntity();
        rel.setZone(zone);
        rel.setServer(server);
        rel.setCreatedAt(OffsetDateTime.now());
        rel.setUpdatedAt(OffsetDateTime.now());
        rel.setStatus(EntityStatus.PENDING);
        persist(rel);
        return rel;
    }

    Optional<ServerEntity> findLeastPopulatedServer() {
        return getEntityManager()
                .createQuery("select s from ServerEntity s left join ZoneServerRelEntity r on r.server = s " +
                                "group by s order by count(r) asc",
                        ServerEntity.class)
                .setMaxResults(1)
                .getResultList()
                .stream()
                .findFirst();
    }
}
