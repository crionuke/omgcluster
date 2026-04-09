package sh.byv.state;

import com.fasterxml.jackson.databind.JsonNode;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import sh.byv.instance.InstanceEntity;

import java.time.OffsetDateTime;
import java.util.Optional;

@ApplicationScoped
public class StateRepository implements PanacheRepository<StateEntity> {

    StateEntity create(final InstanceEntity instance, final StateType type, final JsonNode body) {
        final var state = new StateEntity();
        state.setInstance(instance);
        state.setCreatedAt(OffsetDateTime.now());
        state.setUpdatedAt(OffsetDateTime.now());
        state.setType(type);
        state.setBody(body);
        persist(state);
        return state;
    }

    Optional<StateEntity> findByTypeAndInstance(final StateType type, final InstanceEntity instance) {
        return find("type = ?1 and instance = ?2", type, instance).firstResultOptional();
    }
}
