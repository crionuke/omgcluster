package sh.byv.state;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.exception.NotFoundException;
import sh.byv.node.NodeEntity;
import sh.byv.node.NodeService;
import sh.byv.sim.SimEntity;
import sh.byv.zone.ZoneEntity;

import java.time.OffsetDateTime;
import java.util.function.Consumer;

@Slf4j
@Transactional
@ApplicationScoped
@AllArgsConstructor
public class StateService {

    final StateRepository repository;
    final NodeService nodes;

    public StateEntity create(final NodeEntity node) {
        final var state = repository.create(node, new StateBody());
        log.info("State created for node {}", node.getName());
        return state;
    }

    public StateBody getThisState() {
        final var node = nodes.getThisNode();
        final var state = getByNodeRequired(node);
        return state.getBody();
    }

    public void addZone(final NodeEntity node, final ZoneEntity zone) {
        updateNodeState(node, body -> body.addZone(zone));
        log.info("Added zone {} to node {} state", zone.getId(), node.getId());
    }

    public void removeZone(final NodeEntity node, final Long zoneId) {
        updateNodeState(node, body -> body.removeZone(zoneId));
        log.info("Removed zone {} from node {} state", zoneId, node.getId());
    }

    public void addSim(final NodeEntity node, final SimEntity sim) {
        updateNodeState(node, body -> body.addSim(sim));
        log.info("Added sim {} to node {} state", sim.getId(), node.getId());
    }

    public void removeSim(final NodeEntity node, final Long simId) {
        updateNodeState(node, body -> body.removeSim(simId));
        log.info("Removed sim {} from node {} state", simId, node.getId());
    }

    void updateNodeState(final NodeEntity node, final Consumer<StateBody> mutator) {
        final var state = getByNodeRequired(node);
        mutator.accept(state.getBody());
        state.setUpdatedAt(OffsetDateTime.now());
    }

    StateEntity getByNodeRequired(final NodeEntity node) {
        return repository.findByNode(node)
                .orElseThrow(() -> new NotFoundException("Node %s state not found"
                        .formatted(node.getName())));
    }
}
