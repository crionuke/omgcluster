package sh.byv.state;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sh.byv.instance.InstanceEntity;
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
    final ObjectMapper mapper;

    public StateEntity create(final StateType type) {
        final var body = switch (type) {
            case CLUSTER -> mapper.valueToTree(new ClusterState());
        };

        final var state = repository.create(type, body);
        log.info("State {} created", type);
        return state;
    }

    public StateEntity getOrCreate(final StateType type) {
        final var existing = repository.findByType(type);
        if (existing.isPresent()) {
            log.debug("State {} already exists with id {}", type, existing.get().getId());
            return existing.get();
        }

        return create(type);
    }

    public void addInstance(final InstanceEntity instance) {
        updateClusterState(body -> body.addInstance(instance));
        log.info("Added instance {} to cluster state", instance.getName());
    }

    public void addZone(final InstanceEntity instance, final ZoneEntity zone) {
        updateClusterState(body -> body.addZone(instance, zone));
        log.info("Added zone {} to cluster state", zone.getId());
    }

    public void removeZone(final Long zoneId) {
        updateClusterState(body -> body.removeZone(zoneId));
        log.info("Removed zone {} from cluster state", zoneId);
    }

    public void addSim(final InstanceEntity instance, final SimEntity sim) {
        updateClusterState(body -> body.addSim(instance, sim));
        log.info("Added sim {} to cluster state", sim.getId());
    }

    public void removeSim(final Long simId) {
        updateClusterState(body -> body.removeSim(simId));
        log.info("Removed sim {} from cluster state", simId);
    }

    void updateClusterState(final Consumer<ClusterState> mutator) {
        final var state = getOrCreate(StateType.CLUSTER);
        final var body = mapper.convertValue(state.getBody(), ClusterState.class);
        mutator.accept(body);
        state.setUpdatedAt(OffsetDateTime.now());
        state.setBody(mapper.valueToTree(body));
    }
}
