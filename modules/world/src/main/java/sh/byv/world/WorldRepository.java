package sh.byv.world;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import sh.byv.event.EntityStatus;

import java.time.OffsetDateTime;
import java.util.Optional;

@ApplicationScoped
public class WorldRepository implements PanacheRepository<WorldEntity> {

    public WorldEntity create(final String name) {
        final var world = new WorldEntity();
        world.setCreatedAt(OffsetDateTime.now());
        world.setUpdatedAt(OffsetDateTime.now());
        world.setName(name);
        world.setStatus(EntityStatus.PENDING);
        persist(world);
        return world;
    }

    public Optional<WorldEntity> findByName(final String name) {
        return find("name", name).firstResultOptional();
    }
}
