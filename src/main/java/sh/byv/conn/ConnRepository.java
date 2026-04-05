package sh.byv.conn;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import sh.byv.event.EntityStatus;
import sh.byv.server.ServerEntity;
import sh.byv.world.WorldEntity;

import java.time.OffsetDateTime;

@ApplicationScoped
public class ConnRepository implements PanacheRepository<ConnEntity> {

    public ConnEntity create(final ServerEntity server, final WorldEntity world) {
        final var conn = new ConnEntity();
        conn.setServer(server);
        conn.setWorld(world);
        conn.setCreatedAt(OffsetDateTime.now());
        conn.setUpdatedAt(OffsetDateTime.now());
        conn.setStatus(EntityStatus.PENDING);
        persist(conn);
        return conn;
    }
}
