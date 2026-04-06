package sh.byv.server;

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
public class ServerCreated implements EventHandler {

    final ServerService servers;
    final ServerCreated proxy;

    @Override
    public EventType getType() {
        return EventType.SERVER_CREATED;
    }

    @Override
    public void execute(final EventEntity event) {
        proxy.handle(event.getEntityId());
    }

    @Transactional
    public void handle(final Long serverId) {
        final var server = servers.getByIdRequired(serverId);
        if (server.getStatus() == EntityStatus.PENDING) {
            server.setStatus(EntityStatus.CREATED);
        }
    }
}