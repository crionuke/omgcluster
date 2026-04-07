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

    final WorldRepository worldRepository;
    final EventService eventService;

    public WorldEntity create(final String name) {
        final var world = worldRepository.create(name);
        eventService.create(EventType.WORLD_CREATED, world.getId());
        log.info("Created world {}", name);
        return world;
    }

    public WorldEntity getByIdRequired(final Long id) {
        return worldRepository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("World not found: " + id));
    }

    public WorldEntity getByNameRequired(final String name) {
        return worldRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("World not found: " + name));
    }
}
