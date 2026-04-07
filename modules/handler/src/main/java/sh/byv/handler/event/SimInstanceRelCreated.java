package sh.byv.handler.event;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.event.EntityStatus;
import sh.byv.event.EventEntity;
import sh.byv.event.EventHandler;
import sh.byv.event.EventType;
import sh.byv.sim.SimInstanceRelService;
import sh.byv.state.StateService;

@Slf4j
@Transactional
@ApplicationScoped
@AllArgsConstructor
public class SimInstanceRelCreated implements EventHandler {

    final SimInstanceRelService rels;
    final StateService state;

    @Override
    public EventType getType() {
        return EventType.SIM_INSTANCE_REL_CREATED;
    }

    @Override
    public void execute(final EventEntity event) {
        final var rel = rels.getByIdRequired(event.getEntityId());
        if (rel.getStatus() == EntityStatus.PENDING) {
            rel.setStatus(EntityStatus.CREATED);

            state.addSim(rel.getInstance(), rel.getSim());
        }
    }
}
