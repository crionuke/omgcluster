package sh.byv.zone;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.command.CommandService;
import sh.byv.command.CommandType;
import sh.byv.event.EntityStatus;
import sh.byv.event.EventEntity;
import sh.byv.event.EventHandler;
import sh.byv.event.EventType;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class ZoneServerRelCreated implements EventHandler {

    final ZoneServerRelCreated proxy;
    final ZoneServerRelService rels;
    final CommandService commands;
    final ObjectMapper mapper;

    @Override
    public EventType getType() {
        return EventType.ZONE_SERVER_REL_CREATED;
    }

    @Override
    public void execute(final EventEntity event) {
        proxy.handle(event.getEntityId());
    }

    @Transactional
    public void handle(final Long relId) {
        final var rel = rels.getByIdRequired(relId);
        if (rel.getStatus() == EntityStatus.PENDING) {
            rel.setStatus(EntityStatus.CREATED);

            final var body = mapper.valueToTree(rel.getZone().getId());
            commands.create(rel.getServer().getId(), CommandType.ADD_ZONE, body);
        }
    }
}
