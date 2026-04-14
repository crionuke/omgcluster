package sh.byv.layer.entity;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import sh.byv.world.entity.WorldEntity;

import java.time.OffsetDateTime;
import java.util.Optional;

@ApplicationScoped
public class LayerRepository implements PanacheRepository<LayerEntity> {

    public LayerEntity create(final WorldEntity world, final String name) {
        final var layer = new LayerEntity();
        layer.setWorld(world);
        layer.setCreatedAt(OffsetDateTime.now());
        layer.setUpdatedAt(OffsetDateTime.now());
        layer.setName(name);
        layer.setStatus(LayerStatus.PENDING);
        persist(layer);
        return layer;
    }

    public Optional<LayerEntity> findByWorldAndName(final WorldEntity world, final String name) {
        return find("world = ?1 and name = ?2", world, name).firstResultOptional();
    }
}
