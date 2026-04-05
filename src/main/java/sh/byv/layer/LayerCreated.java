package sh.byv.layer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.event.EntityStatus;
import sh.byv.event.EventEntity;
import sh.byv.event.EventHandler;
import sh.byv.event.EventType;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class LayerCreated implements EventHandler {

    final LayerService layerService;
    final LayerCreated thisHandler;

    @Override
    public EventType getType() {
        return EventType.LAYER_CREATED;
    }

    @Override
    public void execute(final EventEntity event) {
        thisHandler.handle(event.getEntityId());
    }

    @Transactional
    public void handle(final Long layerId) {
        final var layer = layerService.getByIdRequired(layerId);
        if (layer.getStatus() == EntityStatus.PENDING) {
            layer.setStatus(EntityStatus.CREATED);
        }
    }
}
