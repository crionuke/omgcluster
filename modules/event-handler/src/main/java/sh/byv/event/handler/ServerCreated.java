package sh.byv.event.handler;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.event.entity.EventEntity;
import sh.byv.event.entity.EventHandler;
import sh.byv.event.entity.EventType;
import sh.byv.server.entity.ServerService;
import sh.byv.server.entity.ServerStatus;
import sh.byv.state.entity.StateService;

@Slf4j
@Transactional
@ApplicationScoped
@AllArgsConstructor
public class ServerCreated implements EventHandler {

    final ServerService servers;
    final StateService state;

    @Override
    public EventType getType() {
        return EventType.SERVER_CREATED;
    }

    @Override
    public void execute(final EventEntity event) {
        final var server = servers.getByIdRequired(event.getEntityId());
        if (server.getStatus() == ServerStatus.PENDING) {
            state.create(server);

            servers.activate(server);
        }
    }
}
