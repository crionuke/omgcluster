package sh.byv.event.handler;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.cache.service.CacheService;
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
    final CacheService cache;

    @Override
    public EventType getType() {
        return EventType.SERVER_REL_CREATED;
    }

    @Override
    public void execute(final EventEntity event) {
        final var rel = rels.getByIdRequired(event.getEntityId());
        if (rel.getStatus() == ServerRelStatus.PENDING) {
            rels.activate(rel);

            final var serverName = rel.getServer().getName();
            if (rel.getType() == ServerRelType.ZONE) {
                cache.invalidateServerZones(serverName);
            } else if (rel.getType() == ServerRelType.SIM) {
                cache.invalidateServerSims(serverName);
            }
        }
    }
}
