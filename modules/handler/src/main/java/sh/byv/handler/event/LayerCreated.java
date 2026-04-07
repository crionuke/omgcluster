package sh.byv.handler.event;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.event.EntityStatus;
import sh.byv.event.EventEntity;
import sh.byv.event.EventHandler;
import sh.byv.event.EventType;
import sh.byv.layer.LayerService;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class LayerCreated implements EventHandler {

    final LayerService layers;
    final LayerCreated proxy;

    @Override
    public EventType getType() {
        return EventType.LAYER_CREATED;
    }

    @Override
    public void execute(final EventEntity event) {
        proxy.handle(event.getEntityId());
    }

    @Transactional
    public void handle(final Long layerId) {
        final var layer = layers.getByIdRequired(layerId);
        if (layer.getStatus() == EntityStatus.PENDING) {
            layer.setStatus(EntityStatus.CREATED);
        }
    }
}
