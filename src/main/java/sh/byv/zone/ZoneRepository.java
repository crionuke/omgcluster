package sh.byv.zone;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import sh.byv.event.EntityStatus;
import sh.byv.layer.LayerEntity;

import java.time.OffsetDateTime;
import java.util.Optional;

@ApplicationScoped
public class ZoneRepository implements PanacheRepository<ZoneEntity> {

    public ZoneEntity create(final LayerEntity layer,
                             final int x1,
                             final int y1,
                             final int x2,
                             final int y2) {
        final var zone = new ZoneEntity();
        zone.setLayer(layer);
        zone.setCreatedAt(OffsetDateTime.now());
        zone.setUpdatedAt(OffsetDateTime.now());
        zone.setStatus(EntityStatus.PENDING);
        zone.setX1(x1);
        zone.setY1(y1);
        zone.setX2(x2);
        zone.setY2(y2);
        persist(zone);
        return zone;
    }

    public Optional<ZoneEntity> findByLayerAndCoords(final LayerEntity layer,
                                                     final int x1,
                                                     final int y1,
                                                     final int x2,
                                                     final int y2) {
        return find("layer = ?1 and x1 = ?2 and y1 = ?3 and x2 = ?4 and y2 = ?5",
                layer, x1, y1, x2, y2).firstResultOptional();
    }
}
