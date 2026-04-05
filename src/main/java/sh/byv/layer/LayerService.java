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
@ApplicationScoped
@AllArgsConstructor
public class LayerService {

    final LayerRepository layerRepository;
    final EventService eventService;

    @Transactional
    public LayerEntity create(final WorldEntity world, final String name) {
        final var layer = layerRepository.create(world, name);
        eventService.create(EventType.LAYER_CREATED, layer.getId());
        log.info("Created layer {} in world {}", name, world.getName());
        return layer;
    }

    public LayerEntity getRequired(final WorldEntity world, final String name) {
        return layerRepository.findByWorldAndName(world, name)
                .orElseThrow(() -> new NotFoundException("Layer not found: " + name));
    }
}
