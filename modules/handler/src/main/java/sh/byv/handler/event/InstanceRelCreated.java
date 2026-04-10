package sh.byv.handler.event;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.event.EventEntity;
import sh.byv.event.EventHandler;
import sh.byv.event.EventType;
import sh.byv.instance.InstanceRelService;
import sh.byv.instance.InstanceRelStatus;
import sh.byv.sim.SimService;
import sh.byv.state.StateService;
import sh.byv.zone.ZoneService;

@Slf4j
@Transactional
@ApplicationScoped
@AllArgsConstructor
public class InstanceRelCreated implements EventHandler {

    final InstanceRelService rels;
    final StateService state;
    final ZoneService zones;
    final SimService sims;

    @Override
    public EventType getType() {
        return EventType.INSTANCE_REL_CREATED;
    }

    @Override
    public void execute(final EventEntity event) {
        final var rel = rels.getByIdRequired(event.getEntityId());
        if (rel.getStatus() == InstanceRelStatus.PENDING) {
            switch (rel.getType()) {
                case ZONE -> state.addZone(rel.getInstance(), zones.getByIdRequired(rel.getEntityId()));
                case SIM -> {
                    final var sim = sims.getByIdRequired(rel.getEntityId());
                    final var zoneRel = rels.getByZoneRequired(sim.getZone().getId());
                    state.addSim(zoneRel.getInstance(), sim);
                }
            }

            rels.activate(rel);
        }
    }
}
