package sh.byv.event.handler;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.cache.service.CacheEvent;
import sh.byv.event.entity.EventEntity;
import sh.byv.event.entity.EventHandler;
import sh.byv.event.entity.EventType;
import sh.byv.server.entity.ServerRelService;
import sh.byv.server.entity.ServerRelType;
import sh.byv.sim.entity.SimService;
import sh.byv.sim.entity.SimStatus;

@Slf4j
@Transactional
@ApplicationScoped
@AllArgsConstructor
public class SimCreated implements EventHandler {

    final Event<CacheEvent> cacheInvalidation;
    final ServerRelService rels;
    final SimService sims;

    @Override
    public EventType getType() {
        return EventType.SIM_CREATED;
    }

    @Override
    public void execute(final EventEntity event) {
        final var sim = sims.getByIdRequired(event.getEntityId());
        if (sim.getStatus() == SimStatus.PENDING) {
            final var server = rels.getLeastPopulatedServer();
            rels.create(ServerRelType.SIM, sim.getId(), server);

            sims.activate(sim);

            cacheInvalidation.fire(new CacheEvent(CacheEvent.Type.ZONE_SIMS, sim.getZone().getId()));
        }
    }
}
