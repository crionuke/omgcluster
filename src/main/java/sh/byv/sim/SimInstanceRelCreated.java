package sh.byv.sim;

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
public class SimInstanceRelCreated implements EventHandler {

    final SimInstanceRelCreated proxy;
    final SimInstanceRelService rels;
    final CommandService commands;
    final ObjectMapper mapper;

    @Override
    public EventType getType() {
        return EventType.SIM_INSTANCE_REL_CREATED;
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

            final var body = mapper.valueToTree(rel.getSim().getId());
            commands.create(rel.getInstance().getId(), CommandType.ADD_SIM, body);
        }
    }
}
