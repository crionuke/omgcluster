package sh.byv.world;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import sh.byv.event.EventEntity;
import sh.byv.event.EventHandler;
import sh.byv.event.EventType;

@Slf4j
@ApplicationScoped
public class WorldCreated implements EventHandler {

    @Override
    public EventType getType() {
        return EventType.WORLD_CREATED;
    }

    @Override
    public void execute(final EventEntity event) {
        // TODO: implement
    }
}
