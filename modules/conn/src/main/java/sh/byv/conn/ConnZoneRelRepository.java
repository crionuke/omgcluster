package sh.byv.conn;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import sh.byv.zone.ZoneEntity;

import java.time.OffsetDateTime;

@ApplicationScoped
public class ConnZoneRelRepository implements PanacheRepository<ConnZoneRelEntity> {

    ConnZoneRelEntity create(final ConnEntity conn, final ZoneEntity zone) {
        final var rel = new ConnZoneRelEntity();
        rel.setConn(conn);
        rel.setZone(zone);
        rel.setCreatedAt(OffsetDateTime.now());
        rel.setUpdatedAt(OffsetDateTime.now());
        rel.setStatus(ConnZoneRelStatus.PENDING);
        persist(rel);
        return rel;
    }
}
