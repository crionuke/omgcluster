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
import sh.byv.sim.SimService;
import sh.byv.sim.SimStatus;

@Slf4j
@Transactional
@ApplicationScoped
@AllArgsConstructor
public class SimCreated implements EventHandler {

    final SimInstanceRelService rels;
    final EventService events;
    final SimService sims;

    @Override
    public EventType getType() {
        return EventType.SIM_CREATED;
    }

    @Override
    public void execute(final EventEntity event) {
        final var sim = sims.getByIdRequired(event.getEntityId());
        if (sim.getStatus() == SimStatus.PENDING) {
            sim.setStatus(SimStatus.ACTIVE);
            events.create(EventType.SIM_ACTIVATED, sim.getId());

            final var instance = rels.getLeastPopulatedInstance();
            rels.create(sim, instance);
        }
    }
}
