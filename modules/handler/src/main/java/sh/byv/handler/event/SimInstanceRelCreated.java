package sh.byv.handler.event;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.event.EventEntity;
import sh.byv.event.EventHandler;
import sh.byv.event.EventService;
import sh.byv.event.EventType;
import sh.byv.sim.SimInstanceRelService;
import sh.byv.sim.SimInstanceRelStatus;
import sh.byv.state.StateService;

@Slf4j
@Transactional
@ApplicationScoped
@AllArgsConstructor
public class SimInstanceRelCreated implements EventHandler {

    final SimInstanceRelService rels;
    final EventService events;
    final StateService state;

    @Override
    public EventType getType() {
        return EventType.SIM_INSTANCE_REL_CREATED;
    }

    @Override
    public void execute(final EventEntity event) {
        final var rel = rels.getByIdRequired(event.getEntityId());
        if (rel.getStatus() == SimInstanceRelStatus.PENDING) {
            rel.setStatus(SimInstanceRelStatus.ACTIVE);
            events.create(EventType.SIM_INSTANCE_REL_ACTIVATED, rel.getId());

            state.addSim(rel.getInstance(), rel.getSim());
        }
    }
}
