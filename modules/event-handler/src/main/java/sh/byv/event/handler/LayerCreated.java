package sh.byv.event.handler;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.event.entity.EventEntity;
import sh.byv.event.entity.EventHandler;
import sh.byv.event.entity.EventType;
import sh.byv.layer.entity.LayerService;
import sh.byv.layer.entity.LayerStatus;

@Slf4j
@Transactional
@ApplicationScoped
@AllArgsConstructor
public class LayerCreated implements EventHandler {

    final LayerService layers;

    @Override
    public EventType getType() {
        return EventType.LAYER_CREATED;
    }

    @Override
    public void execute(final EventEntity event) {
        final var layer = layers.getByIdRequired(event.getEntityId());
        if (layer.getStatus() == LayerStatus.PENDING) {
            layers.activate(layer);
        }
    }
}
