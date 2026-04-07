package sh.byv.world;

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
public class WorldCreated implements EventHandler {

    final WorldService worlds;
    final WorldCreated proxy;

    @Override
    public EventType getType() {
        return EventType.WORLD_CREATED;
    }

    @Override
    public void execute(final EventEntity event) {
        proxy.handle(event.getEntityId());
    }

    @Transactional
    public void handle(final Long worldId) {
        final var world = worlds.getByIdRequired(worldId);
        if (world.getStatus() == EntityStatus.PENDING) {
            world.setStatus(EntityStatus.CREATED);
        }
    }
}
