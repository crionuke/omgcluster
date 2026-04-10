package sh.byv.handler.event;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.event.EventEntity;
import sh.byv.event.EventHandler;
import sh.byv.event.EventService;
import sh.byv.event.EventType;
import sh.byv.layer.LayerService;
import sh.byv.layer.LayerStatus;

@Slf4j
@Transactional
@ApplicationScoped
@AllArgsConstructor
public class LayerCreated implements EventHandler {

    final LayerService layers;
    final EventService events;

    @Override
    public EventType getType() {
        return EventType.LAYER_CREATED;
    }

    @Override
    public void execute(final EventEntity event) {
        final var layer = layers.getByIdRequired(event.getEntityId());
        if (layer.getStatus() == LayerStatus.PENDING) {
            layer.setStatus(LayerStatus.ACTIVE);
            events.create(EventType.LAYER_ACTIVATED, layer.getId());
        }
    }
}
