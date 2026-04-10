package sh.byv.layer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.event.EventService;
import sh.byv.event.EventType;
import sh.byv.exception.NotFoundException;
import sh.byv.world.WorldEntity;

@Slf4j
@Transactional
@ApplicationScoped
@AllArgsConstructor
public class LayerService {

    final LayerRepository repository;
    final EventService events;

    public LayerEntity create(final WorldEntity world, final String name) {
        final var layer = repository.create(world, name);
        events.create(EventType.LAYER_CREATED, layer.getId());
        log.info("Created layer {} in world {}", name, world.getName());
        return layer;
    }

    public LayerEntity getByIdRequired(final Long id) {
        return repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Layer not found: " + id));
    }

    public LayerEntity getByNameRequired(final WorldEntity world, final String name) {
        return repository.findByWorldAndName(world, name)
                .orElseThrow(() -> new NotFoundException("Layer not found: " + name));
    }

    public void activate(final LayerEntity layer) {
        layer.setStatus(LayerStatus.ACTIVE);
        events.create(EventType.LAYER_ACTIVATED, layer.getId());
    }
}
