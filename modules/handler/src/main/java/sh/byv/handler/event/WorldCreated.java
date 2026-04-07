package sh.byv.handler.event;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.event.EntityStatus;
import sh.byv.event.EventEntity;
import sh.byv.event.EventHandler;
import sh.byv.event.EventType;
import sh.byv.world.WorldService;

@Slf4j
@Transactional
@ApplicationScoped
@AllArgsConstructor
public class WorldCreated implements EventHandler {

    final WorldService worlds;

    @Override
    public EventType getType() {
        return EventType.WORLD_CREATED;
    }

    @Override
    public void execute(final EventEntity event) {
        final var world = worlds.getByIdRequired(event.getEntityId());
        if (world.getStatus() == EntityStatus.PENDING) {
            world.setStatus(EntityStatus.CREATED);
        }
    }
}
