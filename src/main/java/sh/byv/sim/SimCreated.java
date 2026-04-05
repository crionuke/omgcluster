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

    final SimCreated thisHandler;
    final SimService simService;

    @Override
    public EventType getType() {
        return EventType.SIM_CREATED;
    }

    @Override
    public void execute(final EventEntity event) {
        thisHandler.handle(event.getEntityId());
    }

    @Transactional
    public void handle(final Long simId) {
        final var sim = simService.getByIdRequired(simId);
        if (sim.getStatus() == EntityStatus.PENDING) {
            sim.setStatus(EntityStatus.CREATED);
        }
    }
}