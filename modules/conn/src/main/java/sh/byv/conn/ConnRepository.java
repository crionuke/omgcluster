package sh.byv.conn;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import sh.byv.instance.InstanceEntity;
import sh.byv.world.WorldEntity;

import java.time.OffsetDateTime;

@ApplicationScoped
public class ConnRepository implements PanacheRepository<ConnEntity> {

    public ConnEntity create(final InstanceEntity instance, final WorldEntity world) {
        final var conn = new ConnEntity();
        conn.setInstance(instance);
        conn.setWorld(world);
        conn.setCreatedAt(OffsetDateTime.now());
        conn.setUpdatedAt(OffsetDateTime.now());
        conn.setStatus(ConnStatus.PENDING);
        persist(conn);
        return conn;
    }
}
