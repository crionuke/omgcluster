package sh.byv.world.entity;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.OffsetDateTime;
import java.util.Optional;

@ApplicationScoped
public class WorldRepository implements PanacheRepository<WorldEntity> {

    public WorldEntity create(final String name) {
        final var world = new WorldEntity();
        world.setCreatedAt(OffsetDateTime.now());
        world.setUpdatedAt(OffsetDateTime.now());
        world.setName(name);
        world.setStatus(WorldStatus.PENDING);
        persist(world);
        return world;
    }

    public Optional<WorldEntity> findByName(final String name) {
        return find("name", name).firstResultOptional();
    }
}
