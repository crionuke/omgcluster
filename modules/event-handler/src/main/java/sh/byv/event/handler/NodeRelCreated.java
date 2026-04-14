package sh.byv.event.handler;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.event.EventEntity;
import sh.byv.event.EventHandler;
import sh.byv.event.EventType;
import sh.byv.node.NodeRelService;
import sh.byv.node.NodeRelStatus;
import sh.byv.sim.SimService;
import sh.byv.state.StateService;
import sh.byv.zone.ZoneService;

@Slf4j
@Transactional
@ApplicationScoped
@AllArgsConstructor
public class NodeRelCreated implements EventHandler {

    final NodeRelService rels;
    final StateService state;
    final ZoneService zones;
    final SimService sims;

    @Override
    public EventType getType() {
        return EventType.NODE_REL_CREATED;
    }

    @Override
    public void execute(final EventEntity event) {
        final var rel = rels.getByIdRequired(event.getEntityId());
        if (rel.getStatus() == NodeRelStatus.PENDING) {
            switch (rel.getType()) {
                case ZONE -> state.addZone(rel.getNode(), zones.getByIdRequired(rel.getEntityId()));
                case SIM -> {
                    final var sim = sims.getByIdRequired(rel.getEntityId());
                    final var zoneRel = rels.getByZoneRequired(sim.getZone().getId());
                    state.addSim(zoneRel.getNode(), sim);
                }
            }

            rels.activate(rel);
        }
    }
}
