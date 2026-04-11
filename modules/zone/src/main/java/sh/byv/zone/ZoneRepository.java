package sh.byv.zone;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import sh.byv.layer.LayerEntity;

import java.time.OffsetDateTime;

@ApplicationScoped
public class ZoneRepository implements PanacheRepository<ZoneEntity> {

    public ZoneEntity create(final LayerEntity layer,
                             final ZoneEntity parent,
                             final int x1,
                             final int y1,
                             final int x2,
                             final int y2) {
        final var zone = new ZoneEntity();
        zone.setLayer(layer);
        zone.setParent(parent);
        zone.setCreatedAt(OffsetDateTime.now());
        zone.setUpdatedAt(OffsetDateTime.now());
        zone.setStatus(ZoneStatus.PENDING);
        zone.setX1(x1);
        zone.setY1(y1);
        zone.setX2(x2);
        zone.setY2(y2);
        persist(zone);
        return zone;
    }
}
