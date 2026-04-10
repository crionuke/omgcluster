package sh.byv.handler.event;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.event.EventEntity;
import sh.byv.event.EventHandler;
import sh.byv.event.EventType;
import sh.byv.instance.InstanceRelService;
import sh.byv.instance.InstanceRelType;
import sh.byv.sim.SimService;
import sh.byv.sim.SimStatus;

@Slf4j
@Transactional
@ApplicationScoped
@AllArgsConstructor
public class SimCreated implements EventHandler {

    final InstanceRelService rels;
    final SimService sims;

    @Override
    public EventType getType() {
        return EventType.SIM_CREATED;
    }

    @Override
    public void execute(final EventEntity event) {
        final var sim = sims.getByIdRequired(event.getEntityId());
        if (sim.getStatus() == SimStatus.PENDING) {
            final var instance = rels.getLeastPopulatedInstance();
            rels.create(InstanceRelType.SIM, sim.getId(), instance);

            sims.activate(sim);
        }
    }
}
