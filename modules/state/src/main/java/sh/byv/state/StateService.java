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

    public StateEntity create(final InstanceEntity instance, final StateType type) {
        final var body = switch (type) {
            case INSTANCE -> mapper.valueToTree(new InstanceState());
        };

        final var state = repository.create(instance, type, body);
        log.info("State {} created for instance {}", type, instance.getId());
        return state;
    }

    public StateEntity getOrCreate(final InstanceEntity instance, final StateType type) {
        final var existing = repository.findByTypeAndInstance(type, instance);
        if (existing.isPresent()) {
            log.debug("State {} already exists for instance {} with id {}", type, instance.getId(), existing.get().getId());
            return existing.get();
        }

        return create(instance, type);
    }

    public void addZone(final InstanceEntity instance, final ZoneEntity zone) {
        updateInstanceState(instance, body -> body.addZone(zone));
        log.info("Added zone {} to instance {} state", zone.getId(), instance.getId());
    }

    public void removeZone(final InstanceEntity instance, final Long zoneId) {
        updateInstanceState(instance, body -> body.removeZone(zoneId));
        log.info("Removed zone {} from instance {} state", zoneId, instance.getId());
    }

    public void addSim(final InstanceEntity instance, final SimEntity sim) {
        updateInstanceState(instance, body -> body.addSim(sim));
        log.info("Added sim {} to instance {} state", sim.getId(), instance.getId());
    }

    public void removeSim(final InstanceEntity instance, final Long simId) {
        updateInstanceState(instance, body -> body.removeSim(simId));
        log.info("Removed sim {} from instance {} state", simId, instance.getId());
    }

    void updateInstanceState(final InstanceEntity instance, final Consumer<InstanceState> mutator) {
        final var state = getOrCreate(instance, StateType.INSTANCE);
        final var body = mapper.convertValue(state.getBody(), InstanceState.class);
        mutator.accept(body);
        state.setUpdatedAt(OffsetDateTime.now());
        state.setBody(mapper.valueToTree(body));
    }
}
