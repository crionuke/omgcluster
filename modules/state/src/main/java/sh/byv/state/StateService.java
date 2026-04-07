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
            case WORLD -> mapper.valueToTree(new WorldState());
        };

        final var state = repository.create(instance, type, body);
        log.info("State {} created for instance {}", type, instance.getName());
        return state;
    }

    public StateEntity getOrCreate(final InstanceEntity instance, final StateType type) {
        final var existing = repository.findByInstanceAndType(instance, type);
        if (existing.isPresent()) {
            log.debug("State {} already exists with id {}", type, existing.get().getId());
            return existing.get();
        }

        return create(instance, type);
    }

    public void addZone(final InstanceEntity instance, final ZoneEntity zone) {
        updateWorldState(instance, body -> body.getZones().add(zone.getId()));
        log.info("Added zone {} to world state of {}", zone.getId(), instance.getName());
    }

    public void removeZone(final InstanceEntity instance, final Long zoneId) {
        updateWorldState(instance, body -> body.getZones().remove(zoneId));
        log.info("Removed zone {} from world state of {}", zoneId, instance.getName());
    }

    public void addSim(final InstanceEntity instance, final SimEntity sim) {
        updateWorldState(instance, body -> body.getSims().add(sim.getId()));
        log.info("Added sim {} to world state of {}", sim.getId(), instance.getName());
    }

    public void removeSim(final InstanceEntity instance, final Long simId) {
        updateWorldState(instance, body -> body.getSims().remove(simId));
        log.info("Removed sim {} from world state of {}", simId, instance.getName());
    }

    void updateWorldState(final InstanceEntity instance, final Consumer<WorldState> mutator) {
        final var state = getOrCreate(instance, StateType.WORLD);
        final var body = mapper.convertValue(state.getBody(), WorldState.class);
        mutator.accept(body);
        state.setUpdatedAt(OffsetDateTime.now());
        state.setBody(mapper.valueToTree(body));
    }
}