package sh.byv.conn;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import sh.byv.node.NodeEntity;
import sh.byv.world.WorldEntity;

import java.time.OffsetDateTime;

@ApplicationScoped
public class ConnRepository implements PanacheRepository<ConnEntity> {

    public ConnEntity create(final NodeEntity node, final WorldEntity world) {
        final var conn = new ConnEntity();
        conn.setNode(node);
        conn.setWorld(world);
        conn.setCreatedAt(OffsetDateTime.now());
        conn.setUpdatedAt(OffsetDateTime.now());
        conn.setStatus(ConnStatus.PENDING);
        persist(conn);
        return conn;
    }
}
