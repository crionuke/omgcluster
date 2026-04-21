package sh.byv.server.entity;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
class ServerRelRepository implements PanacheRepository<ServerRelEntity> {

    ServerRelEntity create(final ServerEntity server,
                         final ServerRelType type,
                         final Long entityId) {
        final var rel = new ServerRelEntity();
        rel.setServer(server);
        rel.setCreatedAt(OffsetDateTime.now());
        rel.setUpdatedAt(OffsetDateTime.now());
        rel.setType(type);
        rel.setEntityId(entityId);
        rel.setStatus(ServerRelStatus.PENDING);
        persist(rel);
        return rel;
    }

    public List<ServerRelEntity> findRelsByServerAndType(final ServerEntity server, final ServerRelType type) {
        return find("server = ?1 and type = ?2 and status = ?3", server, type, ServerRelStatus.ACTIVE).list();
    }

    Optional<ServerRelEntity> findByTypeAndEntity(final ServerRelType type, final Long entityId) {
        return find("type = ?1 and entityId = ?2", type, entityId).firstResultOptional();
    }

    Optional<ServerEntity> findLeastPopulatedServer() {
        return getEntityManager()
                .createQuery("select s from ServerEntity s left join ServerRelEntity r on r.server = s " +
                                "group by s order by count(r) asc",
                        ServerEntity.class)
                .setMaxResults(1)
                .getResultList()
                .stream()
                .findFirst();
    }
}
