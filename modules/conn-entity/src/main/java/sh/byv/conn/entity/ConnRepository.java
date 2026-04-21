package sh.byv.conn.entity;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import sh.byv.server.entity.ServerEntity;
import sh.byv.world.entity.WorldEntity;

import java.time.OffsetDateTime;

@ApplicationScoped
class ConnRepository implements PanacheRepository<ConnEntity> {

    public ConnEntity create(final ServerEntity server, final WorldEntity world) {
        final var conn = new ConnEntity();
        conn.setServer(server);
        conn.setWorld(world);
        conn.setCreatedAt(OffsetDateTime.now());
        conn.setUpdatedAt(OffsetDateTime.now());
        conn.setStatus(ConnStatus.PENDING);
        persist(conn);
        return conn;
    }
}
