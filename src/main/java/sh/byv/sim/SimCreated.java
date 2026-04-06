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
public class SimCreated implements EventHandler {

    final SimGroupRelService rels;
    final SimCreated proxy;
    final SimService sims;

    @Override
    public EventType getType() {
        return EventType.SIM_CREATED;
    }

    @Override
    public void execute(final EventEntity event) {
        proxy.handle(event.getEntityId());
    }

    @Transactional
    public void handle(final Long simId) {
        final var sim = sims.getByIdRequired(simId);
        if (sim.getStatus() == EntityStatus.PENDING) {
            final var group = rels.getLeastPopulatedGroup();
            rels.create(sim, group);
            sim.setStatus(EntityStatus.CREATED);
        }
    }
}