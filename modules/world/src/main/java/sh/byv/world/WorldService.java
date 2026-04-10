package sh.byv.world;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.event.EventService;
import sh.byv.event.EventType;
import sh.byv.exception.NotFoundException;

@Slf4j
@Transactional
@ApplicationScoped
@AllArgsConstructor
public class WorldService {

    final WorldRepository repository;
    final EventService events;

    public WorldEntity create(final String name) {
        final var world = repository.create(name);
        events.create(EventType.WORLD_CREATED, world.getId());
        log.info("Created world {}", name);
        return world;
    }

    public WorldEntity getByIdRequired(final Long id) {
        return repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("World not found: " + id));
    }

    public WorldEntity getByNameRequired(final String name) {
        return repository.findByName(name)
                .orElseThrow(() -> new NotFoundException("World not found: " + name));
    }

    public void activate(final WorldEntity world) {
        world.setStatus(WorldStatus.ACTIVE);
        events.create(EventType.WORLD_ACTIVATED, world.getId());
    }
}
