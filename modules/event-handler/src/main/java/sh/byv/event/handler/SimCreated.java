package sh.byv.event.handler;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.event.EventEntity;
import sh.byv.event.EventHandler;
import sh.byv.event.EventType;
import sh.byv.node.NodeRelService;
import sh.byv.node.NodeRelType;
import sh.byv.sim.SimService;
import sh.byv.sim.SimStatus;

@Slf4j
@Transactional
@ApplicationScoped
@AllArgsConstructor
public class SimCreated implements EventHandler {

    final NodeRelService rels;
    final SimService sims;

    @Override
    public EventType getType() {
        return EventType.SIM_CREATED;
    }

    @Override
    public void execute(final EventEntity event) {
        final var sim = sims.getByIdRequired(event.getEntityId());
        if (sim.getStatus() == SimStatus.PENDING) {
            final var node = rels.getLeastPopulatedNode();
            rels.create(NodeRelType.SIM, sim.getId(), node);

            sims.activate(sim);
        }
    }
}
