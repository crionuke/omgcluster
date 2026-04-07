package sh.byv.handler.event;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.event.EntityStatus;
import sh.byv.event.EventEntity;
import sh.byv.event.EventHandler;
import sh.byv.event.EventType;
import sh.byv.instance.InstanceService;

@Slf4j
@Transactional
@ApplicationScoped
@AllArgsConstructor
public class InstanceCreated implements EventHandler {

    final InstanceService instances;

    @Override
    public EventType getType() {
        return EventType.INSTANCE_CREATED;
    }

    @Override
    public void execute(final EventEntity event) {
        final var instance = instances.getByIdRequired(event.getEntityId());
        if (instance.getStatus() == EntityStatus.PENDING) {
            instance.setStatus(EntityStatus.CREATED);
        }
    }
}
