package sh.byv.sim;

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
public class SimServerRelCreated implements EventHandler {

    final SimServerRelCreated proxy;
    final SimServerRelService rels;

    @Override
    public EventType getType() {
        return EventType.SIM_SERVER_REL_CREATED;
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
        }
    }
}
