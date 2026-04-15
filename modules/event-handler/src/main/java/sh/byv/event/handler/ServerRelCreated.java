package sh.byv.event.handler;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.event.entity.EventEntity;
import sh.byv.event.entity.EventHandler;
import sh.byv.event.entity.EventType;
import sh.byv.server.entity.ServerRelService;
import sh.byv.server.entity.ServerRelStatus;
import sh.byv.state.entity.StateCache;
import sh.byv.sim.entity.SimService;
import sh.byv.state.entity.StateService;
import sh.byv.zone.entity.ZoneService;

@Slf4j
@Transactional
@ApplicationScoped
@AllArgsConstructor
public class ServerRelCreated implements EventHandler {

    final ServerRelService rels;
    final StateService state;
    final ZoneService zones;
    final SimService sims;

    @Override
    public EventType getType() {
        return EventType.SERVER_REL_CREATED;
    }

    @Override
    public void execute(final EventEntity event) {
        final var rel = rels.getByIdRequired(event.getEntityId());
        if (rel.getStatus() == ServerRelStatus.PENDING) {
            switch (rel.getType()) {
                case ZONE -> state.addZone(rel.getServer(), zones.getByIdRequired(rel.getEntityId()));
                case SIM -> {
                    final var sim = sims.getByIdRequired(rel.getEntityId());
                    final var zoneRel = rels.getByZoneRequired(sim.getZone().getId());
                    state.addSim(zoneRel.getServer(), sim);
                }
            }

            rels.activate(rel);
        }
    }
}
