package sh.byv.event.handler;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.cache.service.CacheEvent;
import sh.byv.event.entity.EventEntity;
import sh.byv.event.entity.EventHandler;
import sh.byv.event.entity.EventType;
import sh.byv.server.entity.ServerRelService;
import sh.byv.server.entity.ServerRelStatus;
import sh.byv.server.entity.ServerRelType;

@Slf4j
@Transactional
@ApplicationScoped
@AllArgsConstructor
public class ServerRelCreated implements EventHandler {

    final ServerRelService rels;
    final Event<CacheEvent> cacheInvalidation;

    @Override
    public EventType getType() {
        return EventType.SERVER_REL_CREATED;
    }

    @Override
    public void execute(final EventEntity event) {
        final var rel = rels.getByIdRequired(event.getEntityId());
        if (rel.getStatus() == ServerRelStatus.PENDING) {
            rels.activate(rel);

            final var serverId = rel.getServer().getId();
            if (rel.getType() == ServerRelType.ZONE) {
                cacheInvalidation.fire(new CacheEvent(CacheEvent.Type.SERVER_ZONES, serverId));
            } else if (rel.getType() == ServerRelType.SIM) {
                cacheInvalidation.fire(new CacheEvent(CacheEvent.Type.SERVER_SIMS, serverId));
            }
        }
    }
}
